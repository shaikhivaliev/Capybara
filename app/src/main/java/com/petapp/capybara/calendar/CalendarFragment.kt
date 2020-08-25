package com.petapp.capybara.calendar

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.petapp.capybara.R
import com.petapp.capybara.extensions.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.view_calendar_day.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private val viewModel: CalendarViewModel by viewModel {
        parametersOf(findNavController())
    }

    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = SimpleDateFormat("LLLL", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendar()
        add_survey.showAdd()
        initObservers()
        add_survey.setOnClickListener { viewModel.openSurveyScreen(null,  null) }
    }


    private fun initObservers() {
        with(viewModel) {
            marks.observe(viewLifecycleOwner, Observer { marks ->
                mark_group.visible(marks.isNotEmpty())
                mark_group.removeAllViews()
                for (mark in marks) {
                    mark_group.addView(createChip(requireContext(), mark))
                }
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun setupCalendar() {

        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        calendar.setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek.first())
        calendar.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val binding = layoutInflater.inflate(R.layout.view_calendar_day, view as ViewGroup, true)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            calendar.notifyDateChanged(day.date)
                            oldDate?.let { calendar.notifyDateChanged(it) }
                        }
                    }
                }
            }
        }
        calendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.calendar_day
                val layout = container.binding.calendar_day_layout
                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.setTextColorRes(android.R.color.black)
                    layout.setBackgroundResource(if (selectedDate == day.date) R.drawable.selected_calendar_day else 0)
                } else {
                    textView.setTextColorRes(R.color.light_gray)
                    layout.background = null
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = layoutInflater.inflate(R.layout.view_calendar_day_header, view as ViewGroup, true)
        }
        calendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {}
        }

        calendar.monthScrollListener = { month ->
            val title = monthTitleFormatter.format(Date(month.year, month.month, 0)).capitalize(Locale.ROOT)
            tv_month.text = title

            selectedDate?.let {
                selectedDate = null
                calendar.notifyDateChanged(it)
            }
        }

        iv_next_month.setOnClickListener {
            calendar.findFirstVisibleMonth()?.let {
                calendar.smoothScrollToMonth(it.yearMonth.next)
            }
        }

        iv_previous_month.setOnClickListener {
            calendar.findFirstVisibleMonth()?.let {
                calendar.smoothScrollToMonth(it.yearMonth.previous)
            }
        }
    }
}


