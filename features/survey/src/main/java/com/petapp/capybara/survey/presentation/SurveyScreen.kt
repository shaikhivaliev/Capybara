package com.petapp.capybara.survey.presentation

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.petapp.capybara.*
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.dialogs.InfoDialog
import com.petapp.capybara.state.ErrorState
import com.petapp.capybara.state.ShowSnackbar
import com.petapp.capybara.survey.R
import com.petapp.capybara.survey.di.SurveyComponentHolder
import com.petapp.capybara.survey.state.SurveyEffect
import com.petapp.capybara.survey.state.SurveyInputData
import com.petapp.capybara.survey.state.SurveyMode
import com.petapp.capybara.survey.state.SurveyUI
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SurveyScreen(
    surveyId: Long? = null,
    openTypes: () -> Unit
) {
    val vm: SurveyVm = SurveyComponentHolder.component.provideSurveyVm()
    vm.getSurvey(surveyId)
    val surveyState by vm.surveyState.collectAsState()
    val sideEffect = vm.sideEffect.collectAsState()
    val input = SurveyInputData()
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            ShowFab(
                state = surveyState,
                input = input,
                vm = vm
            )
        },
        content = {
            when (val state = surveyState) {
                is DataState.DATA -> {
                    ShowSurvey(state.data, input, vm)
                }
                is DataState.ERROR -> ErrorState()
                else -> { // nothing
                }
            }
            when (val effect = sideEffect.value) {
                SurveyEffect.ShowSnackbar ->
                    ShowSnackbar(
                        snackbarHostState = scaffoldState.snackbarHostState,
                        errorMessage = stringResource(R.string.error_empty_data),
                        dismissed = { vm.setEffect(SurveyEffect.Ready) }
                    )
                is SurveyEffect.ShowDeleteDialog ->
                    InfoDialog(
                        title = R.string.survey_delete_explanation,
                        click = { vm.deleteSurvey(effect.surveyId) },
                        dismiss = { vm.setEffect(SurveyEffect.Ready) }
                    )
                is SurveyEffect.NavigateToType -> {
                    openTypes()
                    vm.setEffect(SurveyEffect.Ready)
                }
            }
        }
    )
}

@Composable
private fun ShowFab(
    state: DataState<SurveyMode>?,
    input: SurveyInputData,
    vm: SurveyVm
) {
    state?.onData { mode ->
        FloatingActionButton(
            onClick = {
                when (mode) {
                    is SurveyMode.NEW, is SurveyMode.EDIT -> vm.verifySurvey(mode, input)
                    is SurveyMode.READONLY -> vm.toEditMode(mode.data)
                }
            }) {
            val fabImage = when (mode) {
                is SurveyMode.NEW -> ImageVector.vectorResource(R.drawable.ic_done)
                is SurveyMode.EDIT -> ImageVector.vectorResource(R.drawable.ic_done)
                is SurveyMode.READONLY -> ImageVector.vectorResource(R.drawable.ic_edit)
            }
            Icon(
                imageVector = fabImage,
                contentDescription = null
            )
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun ShowSurvey(
    mode: SurveyMode,
    input: SurveyInputData,
    vm: SurveyVm
) {
    when (mode) {
        is SurveyMode.NEW -> SurveyContent(
            profiles = mode.data.profiles.map { it.name },
            types = mode.data.types.map { it.name },
            input = input,
            vm = vm
        )
        is SurveyMode.EDIT -> {
            with(mode.data) {
                input.survey.value = survey.name
                input.date.value = survey.date
                input.profile.value = profileTitle
                input.type.value = typeTitle
                SurveyContent(
                    profiles = profiles.map { it.name },
                    types = types.map { it.name },
                    input = input,
                    surveyId = survey.id,
                    vm = vm
                )
            }
        }
        is SurveyMode.READONLY -> SurveyContentReadOnly(mode.data)
    }
}

@Composable
fun SurveyContent(
    profiles: List<String>,
    types: List<String>,
    input: SurveyInputData,
    vm: SurveyVm,
    surveyId: Long? = null
) {
    val profilesMenuExpanded = remember { mutableStateOf(false) }
    val typesMenuExpanded = remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val context = LocalContext.current
    Column(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
    ) {
        OutlinedTextFieldOneLine(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            value = input.survey.value,
            onValueChange = {
                input.survey.value = it
            },
            label = stringResource(R.string.survey)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { focus ->
                    if (focus.isFocused) {
                        initCalendar(context) {
                            input.date.value = it
                            focusManager.clearFocus()
                        }
                    }
                },
            value = input.date.value,
            onValueChange = {
                input.date.value = it
            },
            readOnly = true,
            label = { Text(stringResource(R.string.health_diary_survey_date)) }
        )
        ExpandedDropdownMenu(
            label = stringResource(R.string.profile_caps),
            selectedTitle = input.profile.value,
            expanded = profilesMenuExpanded.value,
            selectionOptions = profiles,
            onExpandedChange = {
                profilesMenuExpanded.value = !profilesMenuExpanded.value
            },
            onSelectedText = {
                input.profile.value = it
            }
        )
        ExpandedDropdownMenu(
            label = stringResource(R.string.survey_change_survey_type),
            selectedTitle = input.type.value,
            expanded = typesMenuExpanded.value,
            selectionOptions = types,
            onExpandedChange = {
                typesMenuExpanded.value = !typesMenuExpanded.value
            },
            onSelectedText = {
                input.type.value = it
            }
        )
        if (surveyId != null) {
            DeleteButton(
                title = R.string.survey_delete,
                onClick = {
                    vm.setEffect(SurveyEffect.ShowDeleteDialog(surveyId))
                }
            )
        }
    }
}

@Composable
fun SurveyContentReadOnly(data: SurveyUI) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
    ) {
        OutlinedTextFieldReadOnly(
            value = data.survey.name,
            label = stringResource(R.string.survey)
        )
        OutlinedTextFieldReadOnly(
            value = data.survey.date,
            label = stringResource(R.string.health_diary_survey_date)
        )
        ExpandedDropdownMenuReadOnly(
            value = data.profileTitle,
            label = stringResource(R.string.profile_caps)
        )
        ExpandedDropdownMenuReadOnly(
            value = data.typeTitle,
            label = stringResource(R.string.survey_change_survey_type)
        )
    }
}

private fun initCalendar(
    context: Context,
    onClick: (String) -> Unit
) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
    val calendar: Calendar = Calendar.getInstance()
    val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        calendar.set(year, month, dayOfMonth)
        onClick(dateFormat.format(calendar.time))
    }

    DatePickerDialog(
        context,
        listener,
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}
