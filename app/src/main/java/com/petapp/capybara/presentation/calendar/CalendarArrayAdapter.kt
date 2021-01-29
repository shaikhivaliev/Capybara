package com.petapp.capybara.presentation.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.extensions.currentDateFull
import com.petapp.capybara.widgets.CalendarDayView
import com.petapp.capybara.widgets.CalendarView
import org.jetbrains.annotations.NotNull
import java.util.*

class CalendarArrayAdapter @JvmOverloads constructor(
    @NotNull context: Context,
    private val dates: List<Date>,
    private val currentDate: Calendar,
    private val surveys: List<Survey>
) : ArrayAdapter<Date>(context, R.layout.view_calendar_day) {

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val monthDate = dates[position]
        val calendar = Calendar.getInstance()
        calendar.time = monthDate
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val displayMonth = calendar.get(Calendar.MONTH) + CalendarView.ONE_MONTH
        val displayYear = calendar.get(Calendar.YEAR)
        val currentMonth = currentDate.get(Calendar.MONTH) + CalendarView.ONE_MONTH
        val currentYear = currentDate.get(Calendar.YEAR)

        var view = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.view_calendar_day, parent, false)
        }

        val dayView = view?.findViewById<CalendarDayView>(R.id.calendar_day)

        if (displayMonth == currentMonth && displayYear == currentYear) {
            dayView?.setDayName(day.toString())
            val currentSurveys = surveys.filter {
                it.date == currentDateFull(calendar.time)
            }
            val colors = currentSurveys.map { it.color }
            dayView?.createIndicators(colors)
        }

        return view ?: super.getView(position, convertView, parent)
    }

    override fun getCount(): Int {
        return dates.size
    }

    override fun getPosition(item: Date?): Int {
        return dates.indexOf(item)
    }

    override fun getItem(position: Int): Date? {
        return dates[position]
    }
}
