package com.petapp.capybara.healthdiary.presentation

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
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.dialogs.AddingSurveyDialog
import com.petapp.capybara.dialogs.InfoDialog
import com.petapp.capybara.healthdiary.R
import com.petapp.capybara.healthdiary.di.HealthDiaryComponentHolder
import com.petapp.capybara.healthdiary.state.HealthDiaryEffect
import com.petapp.capybara.healthdiary.state.HealthDiaryUI
import com.petapp.capybara.healthdiary.toUiData
import com.petapp.capybara.list.BaseLazyColumn
import com.petapp.capybara.list.ChipLazyRow
import com.petapp.capybara.list.HealthDiaryHeader
import com.petapp.capybara.list.HealthDiarySurveyItem
import com.petapp.capybara.state.ErrorState
import com.petapp.capybara.styles.textSmall

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HealthDiaryScreen(
    openProfilesScreen: () -> Unit,
) {
    val vm: HealthDiaryVm = HealthDiaryComponentHolder.component.provideViewModel()
    val healthDiaryState = vm.healthDiaryState.collectAsState()
    val sideEffect = vm.sideEffect.collectAsState()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        content = {
            when (val state = healthDiaryState.value) {
                is DataState.DATA -> ShowHealthDiary(
                    state = state.data,
                    vm = vm
                )
                is DataState.ERROR -> ErrorState()
                else -> { // nothing
                }
            }

            when (val effect = sideEffect.value) {
                HealthDiaryEffect.ShowInfoDialog ->
                    InfoDialog(
                        title = R.string.survey_incomplete_data,
                        click = { openProfilesScreen() }
                    )
                is HealthDiaryEffect.ShowAddingSurveyDialog ->
                    AddingSurveyDialog(
                        title = effect.title,
                        add = {
                            // todo
                            vm.createHealthDiarySurvey(null)
                        }
                    )
                is HealthDiaryEffect.ShowDeleteDialog ->
                    InfoDialog(
                        title = R.string.health_diary_delete_survey,
                        click = { vm.deleteHealthDiary(effect.surveyId) }
                    )

            }
        }
    )
}

@Composable
private fun ShowHealthDiary(
    state: HealthDiaryUI,
    vm: HealthDiaryVm
) {
    Column {
        ChipLazyRow(
            chips = state.profiles.toUiData(
                selectedChipId = state.checkedProfileId,
                click = { vm.getHealthDiaryItemsByProfile(it) }
            ))
        BaseLazyColumn {
            state.checkedHealthDiary.forEach { healthDiary ->
                item {
                    HealthDiaryHeader(
                        title = healthDiary.type.title,
                        isExpanded = healthDiary.isExpanded,
                        expand = { vm.expandItem(healthDiary) },
                        addNew = {
                            vm.setEffect(HealthDiaryEffect.ShowAddingSurveyDialog(healthDiary.type.title))
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
                            deleteSurvey = {
                                vm.setEffect(HealthDiaryEffect.ShowDeleteDialog(it))
                            }
                        )
                    }
                }
            }
        }
    }
}
