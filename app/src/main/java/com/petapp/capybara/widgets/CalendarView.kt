package com.petapp.capybara.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.extensions.currentDateFull
import com.petapp.capybara.extensions.currentDateMonthYear
import com.petapp.capybara.extensions.currentMonth
import com.petapp.capybara.presentation.calendar.CalendarArrayAdapter
import com.petapp.capybara.presentation.surveys.SurveysAdapter
import java.util.*
import kotlin.math.abs

@Suppress("ForbiddenComment")
class CalendarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : LinearLayout(context, attributeSet) {

    private val calendar: Calendar = Calendar.getInstance()

    private val gestureDetector: GestureDetector = GestureDetector(context, GestureListener())

    private var month: TextView
    private var days: GridView
    private var listOfDays: ArrayList<Date> = arrayListOf()
    private var listOfSurveyByMonth: ArrayList<Survey> = arrayListOf()
    private val touchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop
    private val velocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity.toFloat()


    private val adapter: SurveysAdapter = SurveysAdapter(
        itemClick = {}
    )

    var onChangeMonthListener: OnChangeMonthListener? = null

    init {
        inflate(context, R.layout.view_calendar, this)
        month = findViewById(R.id.month)
        days = findViewById(R.id.days)

        days.setOnItemClickListener { _, _, position, _ ->
            val currentDate = days.adapter.getItem(position) as Date
            val currentSurveys = listOfSurveyByMonth.filter {
                it.date == currentDateFull(currentDate)
            }
            if (currentSurveys.isNotEmpty()) {
                adapter.items = currentSurveys

                MaterialDialog(context).show {
                    title(text = currentDateFull(currentDate))
                    positiveButton(android.R.string.ok) { this.cancel() }
                    customListAdapter(adapter)
                }
            }
        }
    }

    fun setUpNextMonth() {
        calendar.add(Calendar.MONTH, ONE_MONTH)
        val month = currentMonth(calendar.time)
        onChangeMonthListener?.onChangeMonth(month)
    }

    fun setUpPreviousMonth() {
        calendar.add(Calendar.MONTH, -ONE_MONTH)
        val month = currentMonth(calendar.time)
        onChangeMonthListener?.onChangeMonth(month)
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

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
        return false
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            var result = false
            try {
                val diffX = e2.x - e1.x
                if (abs(diffX) > touchSlop && abs(velocityX) > velocity) {
                    if (diffX > 0) setUpPreviousMonth() else setUpNextMonth()
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }
    }

    interface OnChangeMonthListener {
        fun onChangeMonth(month: String)
    }

    companion object {
        private const val MAX_CALENDAR_DAYS = 42
        private const val ALL_WEEK = 7
        private const val FOR_STARTING_FROM_MONDAY = 2
        const val FIRST_DAY_OF_THE_MONTH = 1
        const val ONE_DAY = 1
        const val ONE_MONTH = 1
    }
}
