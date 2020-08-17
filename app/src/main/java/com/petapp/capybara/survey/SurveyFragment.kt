package com.petapp.capybara.survey

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.petapp.capybara.R
import com.petapp.capybara.UniqueId
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.extensions.argument
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.surveys.SurveysFragment
import kotlinx.android.synthetic.main.fragment_survey.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class SurveyFragment : Fragment(R.layout.fragment_survey) {

    private val viewModel: SurveyViewModel by inject()

    private val surveyId by argument(SURVEY_ID, "")
    private val typeId by argument(TYPE_ID, "")
    private val isNewSurvey by argument(IS_NEW_SURVEY, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        done.showDone()
        if (!isNewSurvey) viewModel.getSurvey(surveyId)
        initObservers()
        initWorkWithDate()
        delete_survey.setOnClickListener { deleteSurvey() }
        done.setOnClickListener { if (isNewSurvey) createSurvey() else updateSurvey() }
    }

    private fun createSurvey() {
        if (isFieldsValid()) {
            val id = UniqueId.id.toString()
            val name = name_et.text.toString()
            val date = survey_date_et.text.toString()
            val survey = Survey(id, typeId, name, date)
            viewModel.createSurvey(survey)
        }
    }

    private fun updateSurvey() {
        if (isFieldsValid()) {
            val name = name_et.text.toString()
            val date = survey_date_et.text.toString()
            val survey = Survey(surveyId, typeId, name, date)
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
        with(viewModel) {
            survey.observe(viewLifecycleOwner, Observer { survey ->
                setSurvey(survey)
            })
            isChangeDone.observe(viewLifecycleOwner, Observer { isDone ->
                if (isDone) navigateBack(typeId)
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
            types.observe(viewLifecycleOwner, Observer { types ->
                for (type in types) {
                    radio_group.addView(creteRadioButton(type.name))
                }
            })
        }
    }

    private fun setSurvey(survey: Survey) {
        name_et.setText(survey.name)
        survey_date_et.setText(survey.date)
    }

    private fun isFieldsValid(): Boolean {
        if (!isNewSurvey) return true
        val name = name_et.text.toString()
        val date = survey_date_et.text.toString()
        return if (name.isNotBlank() && date.isNotBlank()) true
        else {
            name_layout.error = "Пустое имя"
            false
        }
    }

    private fun navigateBack(typeId: String) {
        findNavController().navigate(R.id.surveys, SurveysFragment.createBundle(typeId))
    }

    private fun creteRadioButton(type: String): RadioButton {
        val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(
            MARGIN_START_END,
            MARGIN_TOP_BOTTOM,
            MARGIN_START_END,
            MARGIN_TOP_BOTTOM
        )

        return RadioButton(requireActivity()).apply {
            text = type
            layoutParams = params
            setPadding(PADDING_START, 0, 0, 0)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initWorkWithDate() {

        survey_date_et.setOnClickListener {
            val date = Calendar.getInstance()

            val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                date.set(year, month, dayOfMonth)
                survey_date_et.setText(SimpleDateFormat("dd.MM.yyyy").format(date.time))
            }

            DatePickerDialog(
                requireActivity(),
                listener,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    companion object {
        private const val SURVEY_ID = "SURVEY_ID"
        private const val TYPE_ID = "TYPE_ID"
        private const val IS_NEW_SURVEY = "IS_NEW_SURVEY"
        const val MARGIN_TOP_BOTTOM = 14
        const val MARGIN_START_END = 24
        const val PADDING_START = 24


        fun create(surveyId: String?, typeId: String?, isNew: Boolean): Bundle {
            return Bundle().apply {
                putString(SURVEY_ID, surveyId)
                putString(TYPE_ID, typeId)
                putBoolean(IS_NEW_SURVEY, isNew)
            }
        }
    }
}