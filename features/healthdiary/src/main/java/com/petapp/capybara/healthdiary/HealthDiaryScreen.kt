package com.petapp.capybara.healthdiary

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.petapp.capybara.core.data.model.HealthDiaryType
import com.petapp.capybara.core.data.model.ItemHealthDiary
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.core.vm.viewModel
import com.petapp.capybara.healthdiary.di.HealthDiaryComponentHolder
import com.petapp.capybara.list.BaseLazyColumn
import com.petapp.capybara.list.ChipLazyRow
import com.petapp.capybara.list.HealthDiaryHeader
import com.petapp.capybara.list.HealthDiarySurveyItem
import com.petapp.capybara.state.ErrorState
import com.petapp.capybara.styles.textSmall
import com.petapp.capybara.uicomponents.databinding.DialogHealthDiarySurveyBinding

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HealthDiaryScreen() {
    val vm: HealthDiaryVm = HealthDiaryComponentHolder.getComponent()?.provideViewModel()!!
    val healthDiaryState = vm.healthDiaryState.collectAsState()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        content = {
            when (val state = healthDiaryState.value) {
                DataState.EMPTY -> {
                    // todo showAlertEmptyProfiles { vm.openProfileScreen() }
                }
                is DataState.DATA -> ShowHealthDiary(
                    state = state.data,
                    getHealthDiaryItemsByProfile = { vm.getHealthDiaryItemsByProfile(it) },
                    expandItem = { vm.expandItem(it) }
                )
                is DataState.ERROR -> ErrorState()
                else -> { // nothing
                }
            }
        }
    )
}

@Composable
private fun ShowHealthDiary(
    state: HealthDiaryUI,
    getHealthDiaryItemsByProfile: (Long) -> Unit,
    expandItem: (ItemHealthDiary) -> Unit
) {
    Column {
        ChipLazyRow(
            chips = state.profiles.toUiData(
                selectedChipId = state.checkedProfileId,
                click = { getHealthDiaryItemsByProfile(it) }
            ))
        BaseLazyColumn {
            state.checkedHealthDiary.forEach { healthDiary ->
                val title = when (healthDiary.type) {
                    HealthDiaryType.BLOOD_PRESSURE -> R.string.health_diary_blood_pressure_title
                    HealthDiaryType.BLOOD_GLUCOSE -> R.string.health_diary_blood_glucose_title
                    HealthDiaryType.PULSE -> R.string.health_diary_pulse_title
                    HealthDiaryType.HEIGHT -> R.string.health_diary_height_title
                    HealthDiaryType.WEIGHT -> R.string.health_diary_weight_title
                }
                item {
                    HealthDiaryHeader(
                        title = title,
                        isExpanded = healthDiary.isExpanded,
                        expand = { expandItem(healthDiary) },
                        addNew = {
                            val (title: Int, measure: Int, isBloodPressureType: Boolean) = when (healthDiary.type) {
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
                            // todo
//                            openAddingSurveyDialog(
//                                context = requireContext(),
//                                title = title,
//                                measure = measure,
//                                isBloodPressureType = isBloodPressureType,
//                                onClick = { binding ->
//                                    vm.createHealthDiarySurvey(
//                                        healthDiarySurveyBuilder(
//                                            binding = binding,
//                                            item = healthDiary,
//                                            profileId = state.checkedProfileId
//                                        )
//                                    )
//                                }
//                            )
                        }
                    )
                }
                if (healthDiary.isExpanded && healthDiary.surveys.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(id = R.string.health_diary_empty_survey),
                            style = textSmall()
                        )
                    }
                }
                if (healthDiary.isExpanded && healthDiary.surveys.isNotEmpty()) {
                    items(healthDiary.surveys) { item ->
                        HealthDiarySurveyItem(
                            date = item.date,
                            time = item.time,
                            surveyValue = item.surveyValue,
                            unitOfMeasure = item.unitOfMeasure,
                            id = item.id,
                            deleteSurvey = { openDeleteDialog(item.id) }
                        )
                    }
                }
            }
        }
    }
}

private fun healthDiarySurveyBuilder(
    binding: DialogHealthDiarySurveyBinding,
    item: ItemHealthDiary,
    profileId: Long
) {
    // todo
//    with(binding) {
//        val surveyValue = if (item.type == HealthDiaryType.BLOOD_PRESSURE) {
//            StringBuilder()
//                .append(bloodPressureFirst.text.toString())
//                .append(getString(R.string.health_diary_slash))
//                .append(bloodPressureSecond.text.toString())
//                .toString()
//        } else {
//            surveyValue.text.toString()
//        }
//
//        return SurveyHealthDiary(
//            id = 0L,
//            type = item.type,
//            profileId = profileId,
//            date = surveyDate.text.toString(),
//            time = surveyTime.text.toString(),
//            surveyValue = surveyValue,
//            unitOfMeasure = unitOfMeasure.text.toString()
//        )
//    }
}

private fun openDeleteDialog(surveyId: Long) {
    // todo
//    MaterialDialog(requireActivity()).show {
//        title(text = getString(R.string.health_diary_delete_survey))
//        positiveButton {
//            vm.deleteHealthDiary(surveyId)
//            cancel()
//        }
//        negativeButton { cancel() }
//    }
}
