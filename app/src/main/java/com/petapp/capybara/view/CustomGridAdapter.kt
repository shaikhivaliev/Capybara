package com.petapp.capybara.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.petapp.capybara.R
import org.jetbrains.annotations.NotNull
import java.util.*

class CustomGridAdapter @JvmOverloads constructor(
    @NotNull context: Context,
    private val dates: List<Date>,
    private val currentDate: Calendar
) : ArrayAdapter<Date>(context, R.layout.view_calendar_day) {

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val monthDate = dates[position]
        val calendar = Calendar.getInstance()
        calendar.time = monthDate
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val displayMonth = calendar.get(Calendar.MONTH) + CustomCalendarView.ONE_MONTH
        val displayYear = calendar.get(Calendar.YEAR)
        val currentMonth = currentDate.get(Calendar.MONTH) + CustomCalendarView.ONE_MONTH
        val currentYear = currentDate.get(Calendar.YEAR)

        var view = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.view_calendar_day, parent, false)
        }

        val dayView = view?.findViewById<TextView>(R.id.calendar_day)

        if (displayMonth == currentMonth && displayYear == currentYear) {
            dayView?.text = day.toString()
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
