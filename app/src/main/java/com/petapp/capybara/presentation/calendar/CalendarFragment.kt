package com.petapp.capybara.presentation.calendar

import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.petapp.capybara.R
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.toast
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.petapp.capybara.extensions.currentMonth
import com.petapp.capybara.widgets.CalendarView
import kotlinx.android.synthetic.main.fragment_calendar.add_survey
import kotlinx.android.synthetic.main.fragment_calendar.marks_group
import java.util.*

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private val viewModel: CalendarViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val chipIdToProfileId = mutableMapOf<Int, Long>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObservers()
        viewModel.month.value = currentMonth(Date())
        viewModel.getSurveysByMonth()

        add_survey.setOnClickListener { viewModel.openSurveyScreen(null) }
    }

    private fun initViews() {
        marks_group.setOnCheckedChangeListener { _, checkedId ->
            viewModel.profileId.value = chipIdToProfileId[checkedId]
            viewModel.getSurveysByMonth()
        }
        calendar.onChangeMonthListener = object : CalendarView.OnChangeMonthListener {
            override fun onChangeMonth(month: String) {
                viewModel.month.value = month
                viewModel.getSurveysByMonth()
            }
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
                calendar.setupCalendar(it)
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
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
    }
}
