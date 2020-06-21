package com.petapp.capybara.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.petapp.capybara.R
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.visible
import com.petapp.capybara.surveys.presentation.survey.SurveyFragment
import kotlinx.android.synthetic.main.fragment_calendar.add_survey
import kotlinx.android.synthetic.main.fragment_calendar.mark_group
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private val viewModel: CalendarViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_survey.showAdd()
        viewModel.getMarks()
        initObservers()
        add_survey.setOnClickListener {
            navigateToSurvey(null, null, true)
        }
    }

    private fun initObservers() {
        viewModel.marks.observe(viewLifecycleOwner, Observer { marks ->
            mark_group.visible(marks.isNotEmpty())
            mark_group.removeAllViews()
            for (mark in marks) {
                mark_group.addView(createChip(requireContext(), mark))
            }
        })
    }

    private fun navigateToSurvey(surveyId: String?, typeId: String?, isNewSurvey: Boolean) {
        findNavController().navigate(R.id.survey, SurveyFragment.create(surveyId, typeId, isNewSurvey))
    }
}