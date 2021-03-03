package com.petapp.capybara.presentation.calendar

import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.chip.Chip
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Month
import com.petapp.capybara.data.model.Months
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.toast
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private val viewModel: CalendarViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val monthAdapter by lazy {
        CalendarPagerAdapter(requireContext())
    }

    private val calendar: Calendar = Calendar.getInstance()

    private val chipIdToProfileId = mutableMapOf<Int, Long>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initMonthPager()
        initObservers()
        viewModel.currentDate.value = Calendar.getInstance()
        viewModel.getSurveysByMonth()

        add_survey.setOnClickListener { viewModel.openSurveyScreen(null) }
    }

    private fun initViews() {
        marks_group.setOnCheckedChangeListener { _, checkedId ->
            viewModel.profileId.value = chipIdToProfileId[checkedId]
            viewModel.getSurveysByMonth()
        }
    }

    private fun initMonthPager() {
        month_pager.apply {
            adapter = monthAdapter
            setCurrentItem(1, true)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val currentCalendar = viewModel.currentDate.value
                }
            })
        }
    }

    private fun initObservers() {
        with(viewModel) {
            marks.observe(viewLifecycleOwner, Observer { marks ->
                if (marks.isEmpty()) {
                    showAlertEmptyProfiles()
                } else {
                    for (mark in marks) {
                        val chip = createChip(requireContext(), mark, CHIP_PADDING)
                        marks_group.addView(chip)
                        chipIdToProfileId[chip.id] = mark.id
                    }
                    (marks_group[0] as? Chip)?.isChecked = true
                }
            })
            surveys.observe(viewLifecycleOwner, Observer {
                setupCalendar(it)
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
    }

    private fun setupCalendar(surveys: Months) {
        val previousMonth = (calendar.clone() as Calendar).apply { add(Calendar.MONTH, -ONE_MONTH) }
        val currentMonth = calendar
        val nextMonth = (calendar.clone() as Calendar).apply { add(Calendar.MONTH, ONE_MONTH) }

        val months = listOf(
            Month(surveys.previousMonthSurveys, previousMonth),
            Month(surveys.currentMonthSurveys, currentMonth),
            Month(surveys.nextMonthSurveys, nextMonth)
        )
        monthAdapter.setMonths(months)
    }

    private fun showAlertEmptyProfiles() {
        MaterialDialog(requireActivity())
            .cancelable(false)
            .show {
                title(text = getString(R.string.survey_incomplete_data))
                positiveButton { viewModel.openProfileScreen() }
            }
    }

    companion object {
        const val CHIP_PADDING = 56F
        const val ONE_MONTH = 1
    }
}
