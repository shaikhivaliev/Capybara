package com.petapp.capybara.surveys.presentation.survey

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.petapp.capybara.R
import com.petapp.capybara.common.UniqueId
import com.petapp.capybara.extensions.argument
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.surveys.domain.dto.Survey
import com.petapp.capybara.surveys.presentation.surveys.SurveysFragment
import kotlinx.android.synthetic.main.fragment_survey.*
import org.koin.android.ext.android.inject

class SurveyFragment : Fragment(R.layout.fragment_survey) {

    companion object {
        private const val SURVEY_ID = "SURVEY_ID"
        private const val TYPE_ID = "TYPE_ID"
        private const val IS_NEW_SURVEY = "IS_NEW_SURVEY"

        fun create(surveyId: String?, typeId: String, isNew: Boolean): Bundle {
            return Bundle().apply {
                putString(SURVEY_ID, surveyId)
                putString(TYPE_ID, typeId)
                putBoolean(IS_NEW_SURVEY, isNew)
            }
        }
    }

    private val viewModel: SurveyViewModel by inject()

    private val surveyId by argument(SURVEY_ID, "")
    private val typeId by argument(TYPE_ID, "")
    private val isNewSurvey by argument(IS_NEW_SURVEY, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isNewSurvey) viewModel.getSurvey(surveyId)
        initObservers()
        delete_survey.setOnClickListener { deleteSurvey() }
        done.setOnClickListener { if (isNewSurvey) createSurvey() else updateSurvey() }
    }

    private fun createSurvey() {
        if (isNameValid()) {
            val id = UniqueId.id.toString()
            val name = name_et.text.toString()
            val survey = Survey(id, typeId, name)
            viewModel.createSurvey(survey)
        }
    }

    private fun updateSurvey() {
        if (isNameValid()) {
            val name = name_et.text.toString()
            val survey = Survey(surveyId, typeId, name)
            viewModel.updateSurvey(survey)
        }
    }

    private fun deleteSurvey() {
        val name = name_et.text.toString()
        activity?.let {
            MaterialDialog(it).show {
                if (name.isNotBlank()) {
                    title(text = getString(R.string.cancel_explanation, name))
                } else {
                    title(text = getString(R.string.cancel_explanation_empty))
                }
                positiveButton {
                    if (!isNewSurvey) viewModel.deleteSurvey(surveyId) else navigateBack(typeId)
                    cancel()
                }
                negativeButton { cancel() }
            }
        }
    }

    private fun initObservers() {
        viewModel.survey.observe(viewLifecycleOwner, Observer { survey ->
            setSurvey(survey)
        })
        viewModel.isChangeDone.observe(viewLifecycleOwner, Observer { isDone ->
            if (isDone) navigateBack(typeId)
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            requireActivity().toast(message)
        })
    }

    private fun setSurvey(survey: Survey) {
        name_et.setText(survey.name)
    }

    private fun isNameValid(): Boolean {
        if (!isNewSurvey) return true
        val name = name_et.text.toString()
        return if (name.isNotBlank()) true
        else {
            name_layout.error = "Пустое имя"
            false
        }
    }

    private fun navigateBack(typeId: String) {
        findNavController().navigate(R.id.surveys, SurveysFragment.createBundle(typeId))
    }
}