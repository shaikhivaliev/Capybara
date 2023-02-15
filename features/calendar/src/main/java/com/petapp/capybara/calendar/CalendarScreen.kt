package com.petapp.capybara.calendar

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.petapp.capybara.calendar.di.CalendarComponentHolder
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.core.vm.viewModel
import com.petapp.capybara.list.ChipLazyRow
import com.petapp.capybara.state.ErrorState
import com.petapp.capybara.state.rememberStaticSelectionState
import io.github.boguszpawlowski.composecalendar.Calendar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CalendarScreen(
    openProfileScreen: () -> Unit,
    openNewSurveyScreen: () -> Unit
) {
    val vm: CalendarVm =  CalendarComponentHolder.getComponent()?.provideViewModel()!!
    val calendarState by vm.calendarState.collectAsState()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    openNewSurveyScreen()
                }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                    contentDescription = null
                )
            }
        },
        content = {
            when (val state = calendarState) {
                DataState.EMPTY -> {
                    // todo showAlertEmptyProfiles { openProfileScreen() }
                }
                is DataState.DATA -> ShowCalendar(state.data) { vm.getCheckedSurveys(it) }
                is DataState.ERROR -> ErrorState()
                else -> { // nothing
                }
            }
        }
    )
}

@Composable
private fun ShowCalendar(
    calendarUI: CalendarUI,
    getCheckedSurveys: (Long) -> Unit
) {
    val state = rememberStaticSelectionState()
    state.selectionState.selection = calendarUI.checkedSurveysDates
    Column(
        Modifier.verticalScroll(rememberScrollState())
    ) {
        ChipLazyRow(
            chips = calendarUI.profiles.toUiData(
                selectedChipId = calendarUI.checkedProfileId,
                click = { getCheckedSurveys(it) }
            ))
        Calendar(calendarState = state)
    }
}
