package com.petapp.capybara.presentation.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Month
import com.petapp.capybara.extensions.currentDateFull
import com.petapp.capybara.extensions.currentDateMonthYear
import com.petapp.capybara.presentation.surveys.SurveysAdapter
import kotlinx.android.synthetic.main.item_month.view.*
import java.util.*

class CalendarPagerAdapter(private val context: Context) :
    RecyclerView.Adapter<CalendarPagerAdapter.MonthViewHolder>() {

    private val adapter: SurveysAdapter = SurveysAdapter(
        itemClick = {}
    )

    private val months = mutableListOf<Month>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_month, parent, false)
        return MonthViewHolder(view)
    }

    override fun getItemCount(): Int = months.size

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) = holder.itemView.run {
        setupCalendar(months[position], this)
    }

    fun setMonths(months: List<Month>) {
        this.months.clear()
        this.months.addAll(months)
        notifyDataSetChanged()
    }

    private fun setupCalendar(month: Month, itemView: View) {
        val listOfDays: ArrayList<Date> = arrayListOf()

        val monthCalendar = month.calendar.clone() as Calendar
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
            currentDate = month.calendar,
            surveys = month.surveys
        )

        with(itemView) {
            month_title.text = currentDateMonthYear(month.calendar.time)
            month_view.adapter = adapter
            month_view.setOnItemClickListener { _, _, position, _ ->
                val currentDate = month_view.adapter.getItem(position) as Date
                val currentSurveys = month.surveys.filter {
                    it.date == currentDateFull(currentDate)
                }
                if (currentSurveys.isNotEmpty()) {
                    this@CalendarPagerAdapter.adapter.items = currentSurveys

                    MaterialDialog(context).show {
                        title(text = currentDateFull(currentDate))
                        positiveButton(android.R.string.ok) { this.cancel() }
                        customListAdapter(this@CalendarPagerAdapter.adapter)
                    }
                }
            }
        }
    }

    class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val MAX_CALENDAR_DAYS = 42
        private const val ALL_WEEK = 7
        private const val FOR_STARTING_FROM_MONDAY = 2
        const val FIRST_DAY_OF_THE_MONTH = 1
        const val ONE_DAY = 1
    }
}
