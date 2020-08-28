package com.petapp.capybara.survey

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.chip.Chip
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.createRadioButton
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

    private var typesMap = hashMapOf<String, String>()
    private var type = ""
    private var profile = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        done.showDone()

        initObservers()
        initWorkWithDate()

        args.survey?.id?.apply { viewModel.getSurvey(this) }
        delete_survey.setOnClickListener { deleteSurvey() }
        done.setOnClickListener {
            if (args.survey != null) {
                viewModel.updateSurvey(surveyFactory())
            } else {
                viewModel.createSurvey(surveyFactory())
            }
        }

        types_group.setOnCheckedChangeListener { _, checkedId ->
            val typeButton = view.findViewById<RadioButton>(checkedId)
            if (typeButton != null) {
                type = typeButton.text.toString()
            }
        }

        profiles_group.setOnCheckedChangeListener { _, checkedId ->
            val profileButton = view.findViewById<Chip>(checkedId)
            if (profileButton != null) {
                profile = profileButton.text.toString()
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
                    types_group.addView(requireContext().createRadioButton(type.name))
                    typesMap[type.name] = type.id
                }
            })
            marks.observe(viewLifecycleOwner, Observer { marks ->
                profiles_group.visible(marks.isNotEmpty())
                profiles_group.removeAllViews()
                for (mark in marks) {
                    profiles_group.addView(createChip(requireContext(), mark))
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
        name_et.setText(survey.name)
        survey_date_et.setText(survey.date)
    }

    private fun surveyFactory(): Survey? {
        return if (isFieldsValid()) {
            val id = args.survey?.id ?: ""
            val typeId = args.survey?.typeId ?: typesMap[type] ?: ""
            val profileId = args.survey?.profileId ?: profile
            val name = name_et.text.toString()
            val date = survey_date_et.text.toString()
            return Survey(id = id, typeId = typeId, profileId = profileId, name = name, date = date)
        } else {
            null
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
    }

    private fun isFieldsValid(): Boolean {
        val name = name_et.text.toString()
        val date = survey_date_et.text.toString()
        return if (name.isNotBlank() && date.isNotBlank()) true
        else {
            name_layout.error = requireActivity().getString(R.string.error_get_types)
            false
        }
    }

    companion object {
        const val MARGIN_TOP_BOTTOM = 14
        const val MARGIN_START_END = 24
        const val PADDING_START = 24
    }
}