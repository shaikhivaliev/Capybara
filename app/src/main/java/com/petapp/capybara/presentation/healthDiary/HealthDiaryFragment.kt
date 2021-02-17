package com.petapp.capybara.presentation.healthDiary

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.chip.Chip
import com.petapp.capybara.R
import com.petapp.capybara.data.model.healthDiary.HealthDiaryType
import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary
import com.petapp.capybara.data.model.healthDiary.SurveyHealthDiary
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.presentation.surveys.SurveysFragment
import com.petapp.capybara.presentation.toPresentationModel
import kotlinx.android.synthetic.main.dialog_health_diary_survey.view.*
import kotlinx.android.synthetic.main.fragment_health_diary.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

class HealthDiaryFragment : Fragment(R.layout.fragment_health_diary) {

    private val viewModel: HealthDiaryViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val chipIdToProfileId = mutableMapOf<Int, String>()
    private var profileId: String? = null

    private val adapter: HealthDiaryAdapter =
        HealthDiaryAdapter(
            expandItem = { viewModel.handleStepClick(it) },
            addSurvey = { openAddingSurveyDialog(it) },
            onDelete = { openDeleteDialog(it.id) }
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()

        marks_group.setOnCheckedChangeListener { _, checkedId ->
            profileId = chipIdToProfileId[checkedId]
            viewModel.profileId.value = profileId
            viewModel.getHealthDiaryItems()
        }

        with(recycler_view) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@HealthDiaryFragment.adapter
        }
    }

    private fun openAddingSurveyDialog(item: ItemHealthDiary) {

        val dialog = MaterialDialog(requireContext())
            .customView(R.layout.dialog_health_diary_survey)
            .positiveButton(R.string.save) {
                viewModel.createHealthDiarySurvey(
                    healthDiarySurveyBuilder(
                        view = it.getCustomView(),
                        item = item
                    )
                )
            }
            .negativeButton(R.string.cancel) { it.dismiss() }

        val (title: Int, unitOfMeasure: Int, isBloodPressureType: Boolean) = when (item.type) {
            HealthDiaryType.BLOOD_PRESSURE -> Triple(
                R.string.health_diary_survey_blood_pressure,
                R.string.health_diary_blood_pressure_unit,
                true
            )
            HealthDiaryType.PULSE -> Triple(
                R.string.health_diary_survey_pulse,
                R.string.health_diary_pulse_unit,
                false
            )
            HealthDiaryType.BLOOD_GLUCOSE -> Triple(
                R.string.health_diary_survey_blood_glucose,
                R.string.health_diary_blood_glucose_unit,
                false
            )
            HealthDiaryType.HEIGHT -> Triple(
                R.string.health_diary_survey_height,
                R.string.health_diary_height_unit,
                false
            )
            HealthDiaryType.WEIGHT -> Triple(
                R.string.health_diary_survey_weight,
                R.string.health_diary_weight_unit,
                false
            )
        }
        with(dialog.getCustomView()) {
            initDateAndTime(this)
            initDialogConstraints(this, isBloodPressureType)
            blood_pressure_survey.isVisible = isBloodPressureType
            survey_value.isVisible = !isBloodPressureType

            survey_value_title.text = getString(title)
            unit_of_measure.text = getString(unitOfMeasure)
        }
        dialog.show()
    }

    private fun initDialogConstraints(view: View, isBloodPressureType: Boolean) {
        with(ConstraintSet()) {
            clone(view.dialog_content)
            if (isBloodPressureType) {
                connect(
                    R.id.unit_of_measure,
                    ConstraintSet.BOTTOM,
                    R.id.blood_pressure_second,
                    ConstraintSet.BOTTOM
                )
                connect(R.id.unit_of_measure, ConstraintSet.START, R.id.blood_pressure_second, ConstraintSet.END)
            } else {
                connect(R.id.unit_of_measure, ConstraintSet.BOTTOM, R.id.survey_value, ConstraintSet.BOTTOM)
                connect(R.id.unit_of_measure, ConstraintSet.START, R.id.survey_value, ConstraintSet.END)
            }
            applyTo(view.dialog_content)
        }
    }

    private fun healthDiarySurveyBuilder(view: View, item: ItemHealthDiary): SurveyHealthDiary? {
        val id = DEFAULT_ID_FOR_ENTITY
        val type = item.type
        val profileId = profileId ?: "0"
        val date = view.survey_date.text.toString()
        val time = view.survey_time.text.toString()
        val unitOfMeasure = view.unit_of_measure.text.toString()
        val surveyValue = if (type == HealthDiaryType.BLOOD_PRESSURE) {
            StringBuilder()
                .append(view.blood_pressure_first.text.toString())
                .append(getString(R.string.health_diary_slash))
                .append(view.blood_pressure_second.text.toString())
                .toString()
        } else {
            view.survey_value.text.toString()
        }

        return SurveyHealthDiary(
            id = id,
            type = type,
            profileId = profileId,
            date = date,
            time = time,
            surveyValue = surveyValue,
            unitOfMeasure = unitOfMeasure
        )
    }

    private fun initObservers() {
        with(viewModel) {
            marks.observe(viewLifecycleOwner, Observer { marks ->
                if (marks.isEmpty()) {
                    showAlertEmptyProfiles()
                } else {
                    for (mark in marks) {
                        val chip = createChip(requireContext(), mark, SurveysFragment.CHIP_PADDING)
                        marks_group.addView(chip)
                        chipIdToProfileId[chip.id] = mark.id
                    }
                    (marks_group.get(0) as? Chip)?.isChecked = true
                }
            })
            healthDiaryItems.observe(viewLifecycleOwner, Observer {
                adapter.items = it.toPresentationModel()
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
    }

    private fun openDeleteDialog(surveyId: String) {
        MaterialDialog(requireActivity()).show {
            title(text = getString(R.string.health_diary_delete_survey))
            positiveButton {
                viewModel.deleteHealthDiary(surveyId)
                cancel()
            }
            negativeButton { cancel() }
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

    @SuppressLint("SimpleDateFormat")
    private fun initDateAndTime(view: View) {

        val date = Calendar.getInstance()
        with(view) {
            survey_date.text = SimpleDateFormat("dd.MM.yyyy").format(date.time)
            survey_time.text = SimpleDateFormat("HH:mm").format(date.time)

            survey_date.setOnClickListener {
                val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    date.set(year, month, dayOfMonth)
                    survey_date.text = SimpleDateFormat("dd.MM.yyyy").format(date.time)
                }

                DatePickerDialog(
                    requireActivity(),
                    listener,
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            survey_time.setOnClickListener {
                val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    date.set(hourOfDay, minute)
                    survey_time.text = SimpleDateFormat("HH:mm").format(date.time)
                }

                TimePickerDialog(
                    requireActivity(),
                    listener,
                    date.get(Calendar.HOUR),
                    date.get(Calendar.MINUTE),
                    true
                ).show()
            }
        }
    }

    companion object {
        private const val DEFAULT_ID_FOR_ENTITY = "0"
    }
}
