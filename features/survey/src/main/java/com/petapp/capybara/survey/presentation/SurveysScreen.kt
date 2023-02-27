package com.petapp.capybara.survey.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.dialogs.InfoDialog
import com.petapp.capybara.list.BaseLazyColumn
import com.petapp.capybara.list.ChipLazyRow
import com.petapp.capybara.list.IconTitleDescItem
import com.petapp.capybara.state.EmptyState
import com.petapp.capybara.state.ErrorState
import com.petapp.capybara.survey.R
import com.petapp.capybara.survey.di.SurveyComponentHolder
import com.petapp.capybara.survey.state.SurveysEffect
import com.petapp.capybara.survey.state.SurveysState
import com.petapp.capybara.survey.toUiData

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SurveysScreen(
    typeId: Long?,
    openProfilesScreen: () -> Unit,
    openNewSurveyScreen: () -> Unit,
    openSurveyScreen: (Long) -> Unit
) {
    val vm: SurveysVm = SurveyComponentHolder.component.provideSurveysVm()
    vm.getProfiles(typeId)
    val surveysState by vm.surveysState.collectAsState()
    val sideEffect = vm.sideEffect.collectAsState()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openNewSurveyScreen() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                    contentDescription = null
                )
            }
        },
        content = {
            when (val state = surveysState) {
                is DataState.DATA -> ShowSurveys(
                    state = state.data,
                    getCheckedSurveys = { vm.getCheckedSurveys(it) },
                    openSurveyScreen = openSurveyScreen
                )
                is DataState.ERROR -> ErrorState()
                else -> { // nothing
                }
            }
            when (sideEffect.value) {
                SurveysEffect.ShowInfoDialog ->
                    InfoDialog(
                        title = R.string.survey_incomplete_data,
                        click = { openProfilesScreen() },
                        dismiss = { vm.setEffect(SurveysEffect.Ready) }
                    )
            }
        }
    )
}

@Composable
private fun ShowSurveys(
    state: SurveysState,
    getCheckedSurveys: (Long) -> Unit,
    openSurveyScreen: (Long) -> Unit
) {
    Column {
        ChipLazyRow(
            chips = state.profiles.toUiData(
                selectedChipId = state.checkedProfileId,
                click = {
                    getCheckedSurveys(it)
                }
            ))
        if (state.checkedSurveys.isEmpty()) {
            EmptyState(stringResource(R.string.survey_mock))
        } else {
            BaseLazyColumn {
                items(state.checkedSurveys) { item ->
                    IconTitleDescItem(
                        onClick = { openSurveyScreen(item.id) },
                        item = item.toUiData(),
                        contentScale = ContentScale.Inside
                    )
                }
            }
        }
    }
}
