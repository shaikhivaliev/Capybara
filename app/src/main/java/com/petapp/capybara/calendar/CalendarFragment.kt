package com.petapp.capybara.calendar

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
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
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.daysOfWeekFromLocale
import com.petapp.capybara.extensions.setTextColorRes
import com.petapp.capybara.extensions.visible
import com.petapp.capybara.surveys.presentation.survey.SurveyFragment
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.view_calendar_day.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.WeekFields
import java.util.*

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private val viewModel: CalendarViewModel by viewModel()

    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendar()
        add_survey.showAdd()
        viewModel.getMarks()
        initObservers()
        add_survey.setOnClickListener {
            navigateToSurvey(null, null, true)
        }
    }


    private fun initObservers() {
        viewModel.marks.observe(viewLifecycleOwner, Observer { marks ->
            mark_group.visible(marks.isNotEmpty())
            mark_group.removeAllViews()
            for (mark in marks) {
                mark_group.addView(createChip(requireContext(), mark))
            }
        })
    }

    private fun navigateToSurvey(surveyId: String?, typeId: String?, isNewSurvey: Boolean) {
        findNavController().navigate(R.id.survey, SurveyFragment.create(surveyId, typeId, isNewSurvey))
    }


    private fun setupCalendar(){

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
                    textView.setTextColorRes(R.color.light_black)
                    layout.background = null
                }
            }
        }
//
//        class MonthViewContainer(view: View) : ViewContainer(view) {
//            val legendLayout = layoutInflater.inflate(R.layout.view_calendar_day_header, view as ViewGroup, true)
//        }
//        calendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
//            override fun create(view: View) = MonthViewContainer(view)
//            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
//                // Setup each header day text if we have not done that already.
//                if (container.legendLayout.tag == null) {
//                    container.legendLayout.tag = month.yearMonth
//                    month.yearMonth
//                }
//            }
//        }
//
        calendar.monthScrollListener = { month ->
            val title = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"
            tv_month.text = title

            selectedDate?.let {
                // Clear selection if we scroll to a new month.
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


