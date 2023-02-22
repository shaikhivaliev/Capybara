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
import com.petapp.capybara.core.data.model.Survey
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.dialogs.InfoDialog
import com.petapp.capybara.list.BaseLazyColumn
import com.petapp.capybara.list.ChipLazyRow
import com.petapp.capybara.list.IconTitleDescItem
import com.petapp.capybara.state.EmptyState
import com.petapp.capybara.state.ErrorState
import com.petapp.capybara.survey.R
import com.petapp.capybara.survey.di.SurveyComponentHolder
import com.petapp.capybara.survey.state.SurveysState
import com.petapp.capybara.survey.toUiData

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SurveysScreen(
    openProfilesScreen: () -> Unit,
    openNewSurveyScreen: () -> Unit,
    openSurveyScreen: (Survey) -> Unit
) {
    val vm: SurveysVm = SurveyComponentHolder.component.provideSurveysVm()
    val surveysState by vm.surveysState.collectAsState()
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
                DataState.EMPTY -> {
                    InfoDialog(
                        title = R.string.survey_incomplete_data,
                        click = { openProfilesScreen() },
                        dismiss = { }
                    )
                }
                is DataState.DATA -> ShowSurveys(
                    state = state.data,
                    getCheckedSurveys = { vm.getCheckedSurveys(it) },
                    openSurveyScreen = openSurveyScreen
                )
                is DataState.ERROR -> ErrorState()
                else -> { // nothing
                }
            }
        }
    )
}

@Composable
private fun ShowSurveys(
    state: SurveysState,
    getCheckedSurveys: (Long) -> Unit,
    openSurveyScreen: (Survey) -> Unit
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
                        onClick = { openSurveyScreen(item) },
                        item = item.toUiData(),
                        contentScale = ContentScale.Inside
                    )
                }
            }
        }
    }
}
