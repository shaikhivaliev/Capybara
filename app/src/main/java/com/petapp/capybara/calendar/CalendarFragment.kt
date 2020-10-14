package com.petapp.capybara.calendar

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.petapp.capybara.R
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.daysOfWeekFromLocale
import com.petapp.capybara.extensions.setTextColorRes
import com.petapp.capybara.extensions.toast
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
        add_survey.showAdd()

        initViews(view)
        initObservers()
        setupCalendar()

        add_survey.setOnClickListener { viewModel.openSurveyScreen(null) }
    }

    private fun initViews(view: View) {
        marks_group.setOnCheckedChangeListener { _, checkedId ->
            val marksButton = view.findViewById<Chip>(checkedId)
        }
    }

    private fun initObservers() {
        with(viewModel) {
            marks.observe(viewLifecycleOwner) { marks ->
                for (mark in marks) {
                    marks_group.addView(createChip(requireContext(), mark, CHIP_PADDING))
                }
            }
            errorMessage.observe(viewLifecycleOwner) { error ->
                requireActivity().toast(error)
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun setupCalendar() {

        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        calendar.setup(
            currentMonth.minusMonths(MONTH_TO_SUBSTRACT),
            currentMonth.plusMonths(MONTH_TO_SUBSTRACT),
            daysOfWeek.first()
        )
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
                    textView.setTextColorRes(R.color.teal_50)
                    layout.background = null
                }
            }
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

    companion object {
        const val MONTH_TO_SUBSTRACT = 10L
        const val CHIP_PADDING = 56F
    }
}
