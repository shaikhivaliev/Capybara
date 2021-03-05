package com.petapp.capybara.presentation.calendar

import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.MarginPageTransformer
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

    private var currentCalendar = Calendar.getInstance()

    private val chipIdToProfileId = mutableMapOf<Int, Long>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initMonthPager()
        initObservers()
        viewModel.getInitMonths(Calendar.getInstance())

        add_survey.setOnClickListener { viewModel.openSurveyScreen(null) }
    }

    private fun initViews() {
        marks_group.setOnCheckedChangeListener { _, checkedId ->
            viewModel.profileId.value = chipIdToProfileId[checkedId]
            viewModel.getInitMonths(Calendar.getInstance())
        }
    }

    private fun initMonthPager() {
        month_pager.apply {
            adapter = monthAdapter
            setPageTransformer(MarginPageTransformer(PAGE_MARGIN))
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    currentCalendar = monthAdapter.months[position].calendar

                    if (monthAdapter.itemCount - TWO_MONTH == position) {
                        val month = currentCalendar.clone() as Calendar
                        month.add(Calendar.MONTH, TWO_MONTH)
                        viewModel.getNextMonth(month)
                    }

                    if (position == 1) {
                        val month = currentCalendar.clone() as Calendar
                        month.add(Calendar.MONTH, -TWO_MONTH)
                        viewModel.getPreviousMonth(month)
                    }
                }
            })
        }
    }

    private fun initObservers() {
        with(viewModel) {
            profiles.observe(viewLifecycleOwner, Observer { profiles ->
                if (profiles.isEmpty()) {
                    showAlertEmptyProfiles()
                } else {
                    for (profile in profiles) {
                        val chip = createChip(requireContext(), profile, CHIP_PADDING)
                        marks_group.addView(chip)
                        chipIdToProfileId[chip.id] = profile.id
                    }
                    (marks_group[0] as? Chip)?.isChecked = true
                }
            })
            initMonths.observe(viewLifecycleOwner, Observer {
                setupCalendar(it)
            })
            nextMonth.observe(viewLifecycleOwner, Observer {
                monthAdapter.setNextMonth(it)
            })
            previousMonth.observe(viewLifecycleOwner, Observer {
                monthAdapter.setPreviousMonth(it)
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
    }

    private fun setupCalendar(surveys: Months) {
        val calendar = Calendar.getInstance()
        val twoMonthAgo = (calendar.clone() as Calendar).apply { add(Calendar.MONTH, -TWO_MONTH) }
        val previousMonth = (calendar.clone() as Calendar).apply { add(Calendar.MONTH, -ONE_MONTH) }
        val currentMonth = Calendar.getInstance()
        val nextMonth = (calendar.clone() as Calendar).apply { add(Calendar.MONTH, ONE_MONTH) }
        val nextTwoMonth = (calendar.clone() as Calendar).apply { add(Calendar.MONTH, TWO_MONTH) }

        val months = listOf(
            Month(surveys.twoMonthAgoSurveys, twoMonthAgo),
            Month(surveys.previousMonthSurveys, previousMonth),
            Month(surveys.currentMonthSurveys, currentMonth),
            Month(surveys.nextMonthSurveys, nextMonth),
            Month(surveys.nextTwoMonthSurveys, nextTwoMonth)
        )
        monthAdapter.setInitMonths(months)

        month_pager.setCurrentItem(2, false)
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
        const val PAGE_MARGIN = 120
        const val ONE_MONTH = 1
        const val TWO_MONTH = 2
    }
}
