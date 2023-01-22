package com.petapp.capybara.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.petapp.capybara.R
import com.petapp.capybara.data.model.healthDiary.HealthDiaryType
import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary
import com.petapp.capybara.databinding.DialogHealthDiarySurveyBinding
import java.text.SimpleDateFormat
import java.util.*

fun openAddingSurveyDialog(
    context: Context,
    item: ItemHealthDiary,
    onClick: (DialogHealthDiarySurveyBinding) -> Unit
) {
    val binding = DialogHealthDiarySurveyBinding.inflate(LayoutInflater.from(context))
    val dialog = MaterialDialog(context)
        .customView(view = binding.root)
        .positiveButton(R.string.save) {
            onClick(binding)
        }
        .negativeButton(R.string.cancel) { it.dismiss() }

    val (title: Int, measure: Int, isBloodPressureType: Boolean) = when (item.type) {
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
    with(binding) {
        initDateAndTime(
            context = context,
            binding = this
        )
        initDialogConstraints(this, isBloodPressureType)
        bloodPressureSurvey.isVisible = isBloodPressureType
        surveyValue.isVisible = !isBloodPressureType

        surveyValueTitle.text = context.getString(title)
        unitOfMeasure.text = context.getString(measure)
    }
    dialog.show()
}

private fun initDialogConstraints(binding: DialogHealthDiarySurveyBinding, isBloodPressureType: Boolean) {
    with(ConstraintSet()) {
        clone(binding.dialogContent)
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
        applyTo(binding.dialogContent)
    }
}

@SuppressLint("SimpleDateFormat")
private fun initDateAndTime(
    context: Context,
    binding: DialogHealthDiarySurveyBinding
) {

    val date = Calendar.getInstance()
    with(binding) {
        surveyDate.text = SimpleDateFormat("dd.MM.yyyy").format(date.time)
        surveyTime.text = SimpleDateFormat("HH:mm").format(date.time)

        surveyDate.setOnClickListener {
            val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                date.set(year, month, dayOfMonth)
                surveyDate.text = SimpleDateFormat("dd.MM.yyyy").format(date.time)
            }

            DatePickerDialog(
                context,
                listener,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        surveyTime.setOnClickListener {
            val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                date.set(hourOfDay, minute)
                surveyTime.text = SimpleDateFormat("HH:mm").format(date.time)
            }

            TimePickerDialog(
                context,
                listener,
                date.get(Calendar.HOUR),
                date.get(Calendar.MINUTE),
                true
            ).show()
        }
    }
}
