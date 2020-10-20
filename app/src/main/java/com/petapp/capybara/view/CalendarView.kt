package com.petapp.capybara.view

import android.content.Context
import android.util.AttributeSet
import android.widget.*
import com.petapp.capybara.R
import com.petapp.capybara.calendar.CustomGridAdapter
import com.petapp.capybara.data.model.Survey
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("ForbiddenComment")
class CalendarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : LinearLayout(context, attributeSet) {

    private val calendar: Calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)

    private var nextMonth: ImageView
    private var previousMonth: ImageView
    private var month: TextView
    private var days: GridView
    private var listOfDays: ArrayList<Date> = arrayListOf()

    var onChangeMonthListener: OnChangeMonthListener? = null

    init {
        inflate(context, R.layout.view_calendar, this)
        previousMonth = findViewById(R.id.previous_month)
        nextMonth = findViewById(R.id.next_month)
        month = findViewById(R.id.month)
        days = findViewById(R.id.days)

        previousMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -ONE_MONTH)
            val month = SimpleDateFormat("MMMM", Locale.ENGLISH).format(calendar.time)
            onChangeMonthListener?.onChangeMonth(month)
        }

        nextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, ONE_MONTH)
            val month = SimpleDateFormat("MMMM", Locale.ENGLISH).format(calendar.time)
            onChangeMonthListener?.onChangeMonth(month)
        }

        // days.setOnItemClickListener { parent, view, position, id -> }
    }

    fun setupCalendar(surveys: List<Survey>) {
        val currentDate = dateFormat.format(calendar.time)
        month.text = currentDate
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

        val adapter = CustomGridAdapter(
            context,
            dates = listOfDays,
            currentDate = calendar,
            surveys = surveys
        )
        days.adapter = adapter
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
