package com.petapp.capybara.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.petapp.capybara.R
import com.petapp.capybara.presentation.calendar.CalendarArrayAdapter
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.extensions.currentDateFull
import com.petapp.capybara.extensions.currentDateMonthYear
import com.petapp.capybara.extensions.currentMonth
import com.petapp.capybara.presentation.surveys.SurveysAdapterDelegate
import java.util.*

@Suppress("ForbiddenComment")
class CalendarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : LinearLayout(context, attributeSet) {

    private val calendar: Calendar = Calendar.getInstance()

    private var nextMonth: ImageView
    private var previousMonth: ImageView
    private var month: TextView
    private var days: GridView
    private var listOfDays: ArrayList<Date> = arrayListOf()
    private var listOfSurveyByMonth: ArrayList<Survey> = arrayListOf()

    private val adapter: CalendarSurveysAdapter by lazy { CalendarSurveysAdapter() }

    var onChangeMonthListener: OnChangeMonthListener? = null

    init {
        inflate(context, R.layout.view_calendar, this)
        previousMonth = findViewById(R.id.previous_month)
        nextMonth = findViewById(R.id.next_month)
        month = findViewById(R.id.month)
        days = findViewById(R.id.days)

        previousMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -ONE_MONTH)
            val month = currentMonth(calendar.time)
            onChangeMonthListener?.onChangeMonth(month)
        }

        nextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, ONE_MONTH)
            val month = currentMonth(calendar.time)
            onChangeMonthListener?.onChangeMonth(month)
        }

        days.setOnItemClickListener { _, _, position, _ ->
            val currentDate = days.adapter.getItem(position) as Date
            val currentSurveys = listOfSurveyByMonth.filter {
                it.date == currentDateFull(currentDate)
            }
            if (currentSurveys.isNotEmpty()) {
                adapter.setDataSet(currentSurveys)

                MaterialDialog(context).show {
                    title(text = currentDateFull(currentDate))
                    positiveButton(android.R.string.ok) { this.cancel() }
                    customListAdapter(adapter)
                }
            }
        }
    }

    fun setupCalendar(surveys: List<Survey>) {
        listOfSurveyByMonth.clear()
        listOfSurveyByMonth.addAll(surveys)
        month.text = currentDateMonthYear(calendar.time)
        listOfDays.clear()

        val monthCalendar = calendar.clone() as Calendar
        monthCalendar.set(Calendar.DAY_OF_MONTH, FIRST_DAY_OF_THE_MONTH)

        val firstDayOfMonthInWeek = monthCalendar.get(Calendar.DAY_OF_WEEK) - FOR_STARTING_FROM_MONDAY
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonthInWeek)
        if (firstDayOfMonthInWeek < 0) {
            monthCalendar.add(Calendar.DAY_OF_MONTH, -ALL_WEEK)
        }

        while (listOfDays.size < MAX_CALENDAR_DAYS) {
            listOfDays.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, ONE_DAY)
        }

        val adapter = CalendarArrayAdapter(
            context,
            dates = listOfDays,
            currentDate = calendar,
            surveys = surveys
        )
        days.adapter = adapter
    }

    inner class CalendarSurveysAdapter : ListDelegationAdapter<MutableList<Any>>() {
        init {
            items = mutableListOf()
            delegatesManager
                .addDelegate(
                    SurveysAdapterDelegate(
                        itemClick = {}
                    )
                )
        }

        fun setDataSet(surveys: List<Survey>) {
            items.clear()
            items.addAll(surveys)
            notifyDataSetChanged()
        }
    }

    companion object {
        private const val MAX_CALENDAR_DAYS = 42
        private const val ALL_WEEK = 7
        private const val FOR_STARTING_FROM_MONDAY = 2
        const val FIRST_DAY_OF_THE_MONTH = 1
        const val ONE_DAY = 1
        const val ONE_MONTH = 1
    }

    interface OnChangeMonthListener {
        fun onChangeMonth(month: String)
    }
}
