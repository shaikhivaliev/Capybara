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
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.extensions.visible
import kotlinx.android.synthetic.main.fragment_survey.*
import kotlinx.android.synthetic.main.fragment_survey.mark_group
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

class SurveyFragment : Fragment(R.layout.fragment_survey) {

    private val viewModel: SurveyViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val args: SurveyFragmentArgs by navArgs()

    private var typesMap = hashMapOf<String, String>()
    private var type = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        done.showDone()
        initObservers()
        initWorkWithDate()
        args.survey?.id?.apply { viewModel.getSurvey(this) }
        delete_survey.setOnClickListener { deleteSurvey() }
        done.setOnClickListener { if (args.survey != null) updateSurvey() else createSurvey() }
        radio_group.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = view.findViewById<RadioButton>(checkedId)
            if (radioButton != null) {
                type = radioButton.text.toString()
            }
        }
    }

    private fun createSurvey() {
        if (isFieldsValid()) {
            val name = name_et.text.toString()
            val date = survey_date_et.text.toString()
            if (!type.isBlank() && typesMap.containsKey(type)){
                val survey = Survey(null, typesMap[type]!!, name, date)
                viewModel.createSurvey(survey)
            }
        }
    }

    private fun updateSurvey() {
        if (isFieldsValid()) {
            val name = name_et.text.toString()
            val date = survey_date_et.text.toString()
            if (args.survey != null) {
                val survey = Survey(args.survey?.id, args.survey?.typeId!!, name, date)
                viewModel.updateSurvey(survey)
            }
        }
    }

    private fun deleteSurvey() {
        val name = name_et.text.toString()
        activity?.let {
            MaterialDialog(it).show {
                if (name.isNotBlank()) {
                    title(text = getString(R.string.profile_delete_explanation, name))
                } else {
                    title(text = getString(R.string.profile_delete_explanation_empty))
                }
                positiveButton {
                    if (args.survey != null) {
                        viewModel.deleteSurvey(args.survey?.id!!)
                    } else {
                        viewModel.back()
                    }
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
            types.observe(viewLifecycleOwner, Observer { types ->
                for (type in types) {
                    radio_group.addView(creteRadioButton(type.name))
                    type.id?.apply { typesMap[type.name] = this }
                }
            })
            marks.observe(viewLifecycleOwner, Observer { marks ->
                mark_group.visible(marks.isNotEmpty())
                mark_group.removeAllViews()
                for (mark in marks) {
                    mark_group.addView(createChip(requireContext(), mark))
                }
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
    }

    private fun setSurvey(survey: Survey) {
        name_et.setText(survey.name)
        survey_date_et.setText(survey.date)
    }

    private fun isFieldsValid(): Boolean {
        if (args.survey != null) return true
        val name = name_et.text.toString()
        val date = survey_date_et.text.toString()
        return if (name.isNotBlank() && date.isNotBlank()) true
        else {
            name_layout.error = "Пустое имя"
            false
        }
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
            tag = type
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
        const val MARGIN_TOP_BOTTOM = 14
        const val MARGIN_START_END = 24
        const val PADDING_START = 24
    }
}