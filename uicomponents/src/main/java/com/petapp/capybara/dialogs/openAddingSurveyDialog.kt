package com.petapp.capybara.dialogs

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.constraintlayout.widget.ConstraintSet
import com.petapp.capybara.uicomponents.R
import com.petapp.capybara.uicomponents.databinding.DialogHealthDiarySurveyBinding
import java.text.SimpleDateFormat
import java.util.*

fun openAddingSurveyDialog(
    context: Context,
    title: Int,
    measure: Int,
    isBloodPressureType: Boolean,
    onClick: () -> Unit
) {
    // todo
//    val binding = DialogHealthDiarySurveyBinding.inflate(LayoutInflater.from(context))
//    val dialog = MaterialDialog(context)
//        .customView(view = binding.root)
//        .positiveButton(R.string.save) {
//            onClick()
//        }
//        .negativeButton(R.string.cancel) { it.dismiss() }
//
//    with(binding) {
//        initDateAndTime(
//            context = context,
//            binding = this
//        )
//        initDialogConstraints(this, isBloodPressureType)
//        bloodPressureSurvey.isVisible = isBloodPressureType
//        surveyValue.isVisible = !isBloodPressureType
//
//        surveyValueTitle.text = context.getString(title)
//        unitOfMeasure.text = context.getString(measure)
//    }
//    dialog.show()
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
