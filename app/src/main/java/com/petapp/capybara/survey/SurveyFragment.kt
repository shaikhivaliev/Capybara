package com.petapp.capybara.survey

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.extensions.currentMonth
import com.petapp.capybara.extensions.showKeyboard
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.extensions.visible
import kotlinx.android.synthetic.main.fragment_survey.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

class SurveyFragment : Fragment(R.layout.fragment_survey) {

    private val viewModel: SurveyViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val args: SurveyFragmentArgs by navArgs()

    private var typeName = ""
    private var marksName = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initObservers()
        initWorkWithDate()

        args.survey?.id?.apply { viewModel.getSurvey(this) }

        if (args.survey?.id == null) {
            current_survey.visible(false)
            edit_survey.visible(true)
            survey_name_et.requestFocus()
            survey_name_et.showKeyboard()
        }
    }

    private fun initViews(view: View) {
        survey_name_et.doAfterTextChanged { survey_name_layout.error = null }
        survey_date_et.doAfterTextChanged { survey_date_layout.error = null }

        delete_survey.setOnClickListener { deleteSurvey() }

        done.setOnClickListener {
            if (args.survey != null) {
                viewModel.updateSurvey(surveyFactory())
            } else {
                viewModel.createSurvey(surveyFactory())
            }
        }

//        types_group.setOnCheckedChangeListener { _, checkedId ->
//            val typeButton = view.findViewById<RadioButton>(checkedId)
//            if (typeButton != null) {
//                typeName = typeButton.text.toString()
//            }
//        }
//
//        marks_group.setOnCheckedChangeListener { _, checkedId ->
//            val marksButton = view.findViewById<Chip>(checkedId)
//            if (marksButton != null) {
//                marksName = marksButton.text.toString()
//            }
//        }
    }

    private fun initObservers() {
        with(viewModel) {
            survey.observe(viewLifecycleOwner, Observer { survey ->
                setSurvey(survey)
            })
            types.observe(viewLifecycleOwner, Observer { types ->
                if (types.isEmpty()) showAlertCreateSurvey(getString(R.string.type))
                for (type in types) {
                    // types_group.addView(requireContext().createRadioButton(type.name))
                }
            })
            marks.observe(viewLifecycleOwner, Observer { marks ->
                if (marks.isEmpty()) showAlertCreateSurvey(getString(R.string.profile))
                for (mark in marks) {
                    // marks_group.addView(createChip(requireContext(), mark, CHIP_PADDING))
                }
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
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

    private fun setSurvey(survey: Survey) {
        survey_name_et.setText(survey.name)
        survey_date_et.setText(survey.date)
    }

    private fun surveyFactory(): Survey? {
        return if (isFieldsValid()) {
            val id = args.survey?.id ?: DEFAULT_ID_FOR_ENTITY
            val typeId = viewModel.types.value?.find { it.name == typeName }?.id ?: ""
            val profileId = viewModel.marks.value?.find { it.name == marksName }?.id ?: ""
            val color = viewModel.marks.value?.find { it.name == marksName }?.color ?: 0
            val name = survey_name_et.text.toString()
            val date = survey_date_et.text.toString()
            val time = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(date)
            val calendar = Calendar.getInstance()
            calendar.time = time!!
            val month = currentMonth(calendar.time)
            return Survey(
                id = id,
                typeId = typeId,
                profileId = profileId,
                color = color,
                name = name,
                date = date,
                month = month
            )
        } else {
            null
        }
    }

    private fun deleteSurvey() {
        val name = survey_name_et.text.toString()
        MaterialDialog(requireActivity()).show {
            if (name.isNotBlank()) {
                title(text = getString(R.string.survey_delete_explanation, name))
            } else {
                title(text = getString(R.string.survey_delete_explanation_empty))
            }
            positiveButton {
                if (args.survey?.id != null) {
                    viewModel.deleteSurvey(args.survey?.id!!)
                } else {
                    viewModel.back()
                }
                cancel()
            }
            negativeButton { cancel() }
        }
    }

    private fun isFieldsValid(): Boolean {
        return isNameValid() && isDateValid() && isTypeSelected() && isProfileSelected()
    }

    private fun isNameValid(): Boolean {
        return if (survey_name_et.text.toString().isNotBlank()) true
        else {
            survey_name_layout.error = requireActivity().getString(R.string.error_empty_name)
            false
        }
    }

    private fun isDateValid(): Boolean {
        return if (survey_date_et.text.toString().isNotBlank()) true
        else {
            survey_date_layout.error = requireActivity().getString(R.string.error_empty_date)
            false
        }
    }

    private fun isTypeSelected(): Boolean {
        return if (typeName.isNotBlank()) true
        else {
            requireActivity().toast(R.string.error_empty_type)
            false
        }
    }

    private fun isProfileSelected(): Boolean {
        return if (marksName.isNotBlank()) true
        else {
            requireActivity().toast(R.string.error_empty_profile)
            false
        }
    }

    private fun showAlertCreateSurvey(incompleteDataName: String) {
        MaterialDialog(requireActivity()).show {
            title(text = getString(R.string.survey_incomplete_data, incompleteDataName))
            positiveButton {
                cancel()
            }
        }
    }

    companion object {
        const val MARGIN_TOP_BOTTOM = 14
        const val MARGIN_START_END = 24
        const val PADDING_START = 24
        const val DEFAULT_ID_FOR_ENTITY = "0"
        const val CHIP_PADDING = 56F
    }
}
