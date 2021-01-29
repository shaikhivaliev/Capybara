package com.petapp.capybara.presentation.healthDiary

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.petapp.capybara.R
import com.petapp.capybara.adapter.StandardListAdapter
import com.petapp.capybara.adapter.emptySurveyHealthDiaryDelegate
import com.petapp.capybara.adapter.itemHealthDiaryDelegate
import com.petapp.capybara.data.model.HealthDiaryType
import com.petapp.capybara.data.model.ItemHealthDiary
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.presentation.toPresentationModel
import kotlinx.android.synthetic.main.dialog_health_diary_survey.view.*
import kotlinx.android.synthetic.main.fragment_health_diary.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HealthDiaryFragment : Fragment(R.layout.fragment_health_diary) {

    private val viewModel: HealthDiaryViewModel by viewModel()

    private val adapter by lazy {
        StandardListAdapter(
            itemHealthDiaryDelegate(
                expandItem = { viewModel.handleStepClick(it) },
                addSurvey = { openAddingSurveyDialog(it) }
            ),
            emptySurveyHealthDiaryDelegate()
        )
    }

    private fun openAddingSurveyDialog(item: ItemHealthDiary) {

        val dialog = MaterialDialog(requireContext())
            .customView(R.layout.dialog_health_diary_survey)
            .positiveButton(R.string.save) {
                // viewModel.addSurvey()
            }
            .negativeButton(R.string.cancel) { it.dismiss() }
        val view = dialog.getCustomView()

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
        with(view) {
            blood_pressure_survey.isVisible = isBloodPressureType
            survey_value.isVisible = !isBloodPressureType

            with(ConstraintSet()) {
                clone(dialog_content)
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
                applyTo(dialog_content)
            }

            survey_value_title.text = getString(title)
            unit_of_measure.text = getString(unitOfMeasure)
        }
        dialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        with(recycler_view) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@HealthDiaryFragment.adapter
        }
    }

    private fun initObservers() {
        with(viewModel) {
            healthDiaryItems.observe(viewLifecycleOwner, Observer {
                adapter.items = it.toPresentationModel()
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
            expandedItem.observe(viewLifecycleOwner, Observer { item ->
            })
        }
    }
}
