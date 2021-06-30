package com.petapp.capybara.presentation.calendar

import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.chip.Chip
import com.petapp.capybara.R
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.data.model.Month
import com.petapp.capybara.data.model.Months
import com.petapp.capybara.databinding.FragmentCalendarBinding
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.presentation.main.MainActivity
import java.util.*
import javax.inject.Inject

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private val viewBinding by viewBinding(FragmentCalendarBinding::bind)

    @Inject
    lateinit var vmFactory: CalendarVmFactory

    private val vm: CalendarVm by stateViewModel(
        vmFactoryProducer = { vmFactory }
    )

    private val monthAdapter by lazy {
        CalendarPagerAdapter(requireContext())
    }

    private var currentCalendar = Calendar.getInstance()

    private val chipIdToProfileId = mutableMapOf<Int, Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeaturesComponentHolder.getComponent(requireActivity() as MainActivity)?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initMonthPager()
        initObservers()
        vm.getInitMonths(Calendar.getInstance())

        viewBinding.addSurvey.setOnClickListener { vm.openSurveyScreen(null) }
    }

    private fun initViews() {
        viewBinding.marksGroup.setOnCheckedChangeListener { _, checkedId ->
            vm.profileId.value = chipIdToProfileId[checkedId]
            vm.getInitMonths(Calendar.getInstance())
        }
    }

    private fun initMonthPager() {
        viewBinding.monthPager.apply {
            adapter = monthAdapter
            setPageTransformer(MarginPageTransformer(PAGE_MARGIN))
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    currentCalendar = monthAdapter.months[position].calendar

                    if (monthAdapter.itemCount - TWO_MONTH == position) {
                        val month = currentCalendar.clone() as Calendar
                        month.add(Calendar.MONTH, TWO_MONTH)
                        vm.getNextMonth(month)
                    }

                    if (position == 1) {
                        val month = currentCalendar.clone() as Calendar
                        month.add(Calendar.MONTH, -TWO_MONTH)
                        vm.getPreviousMonth(month)
                    }
                }
            })
        }
    }

    private fun initObservers() {
        with(vm) {
            profiles.observe(viewLifecycleOwner, { profiles ->
                if (profiles.isEmpty()) {
                    showAlertEmptyProfiles()
                } else {
                    for (profile in profiles) {
                        val chip = createChip(requireContext(), profile, CHIP_PADDING)
                        viewBinding.marksGroup.addView(chip)
                        chipIdToProfileId[chip.id] = profile.id
                    }
                    (viewBinding.marksGroup[0] as? Chip)?.isChecked = true
                }
            })
            initMonths.observe(viewLifecycleOwner, {
                setupCalendar(it)
            })
            nextMonth.observe(viewLifecycleOwner, {
                monthAdapter.setNextMonth(it)
            })
            previousMonth.observe(viewLifecycleOwner, {
                monthAdapter.setPreviousMonth(it)
            })
            errorMessage.observe(viewLifecycleOwner, { error ->
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
        monthAdapter.updateMonths(months)
        viewBinding.monthPager.setCurrentItem(2, false)
    }

    private fun showAlertEmptyProfiles() {
        MaterialDialog(requireActivity())
            .cancelable(false)
            .show {
                title(text = getString(R.string.survey_incomplete_data))
                positiveButton { vm.openProfileScreen() }
            }
    }

    companion object {
        const val CHIP_PADDING = 56F
        const val PAGE_MARGIN = 120
        const val ONE_MONTH = 1
        const val TWO_MONTH = 2
    }
}
