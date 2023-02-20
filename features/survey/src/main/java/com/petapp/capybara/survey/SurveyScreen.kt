package com.petapp.capybara.survey

import android.annotation.SuppressLint
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.petapp.capybara.*
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.core.mvi.SideEffect
import com.petapp.capybara.state.ErrorState
import com.petapp.capybara.state.ShowSnackbar
import com.petapp.capybara.survey.di.SurveyComponentHolder
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SurveyScreen() {
    val vm: SurveyVm = SurveyComponentHolder.component.provideSurveyVm()
    val surveyState by vm.surveyState.collectAsState()
    val sideEffect by vm.sideEffect.collectAsState()
    val input = SurveyInputData()
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            ShowFab(
                state = surveyState,
                input = input,
                verifySurvey = { mode, input -> vm.verifySurvey(mode, input) },
                toEditMode = { vm.toEditMode(it) }
            )
        },
        content = {
            when (val state = surveyState) {
                is DataState.DATA -> {
                    ShowSurvey(state.data, input)
                }
                is DataState.ERROR -> ErrorState()
                else -> { // nothing
                }
            }
            if (sideEffect is SideEffect.ACTION) {
                ShowSnackbar(
                    snackbarHostState = scaffoldState.snackbarHostState,
                    errorMessage = stringResource(R.string.error_empty_data),
                    dismissed = { vm.dismissSnackbar() }
                )
            }
        }
    )
}

@Composable
private fun ShowFab(
    state: DataState<SurveyMode>?,
    verifySurvey: (SurveyMode, SurveyInputData) -> Unit,
    toEditMode: (SurveyUI) -> Unit,
    input: SurveyInputData
) {
    state?.onData { mode ->
        FloatingActionButton(
            onClick = {
                when (mode) {
                    is SurveyMode.NEW, is SurveyMode.EDIT -> verifySurvey(
                        mode,
                        input
                    )
                    is SurveyMode.READONLY -> toEditMode(mode.data)
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
    input: SurveyInputData
) {
    when (mode) {
        is SurveyMode.NEW -> SurveyContent(
            profiles = mode.data.profiles.map { it.name },
            types = mode.data.types.map { it.name },
            surveyInputData = input
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
                    surveyInputData = input,
                    surveyId = survey.id
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
    surveyInputData: SurveyInputData,
    surveyId: Long? = null
) {
    val profilesMenuExpanded = remember { mutableStateOf(false) }
    val typesMenuExpanded = remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    Column(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
    ) {
        OutlinedTextFieldOneLine(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            value = surveyInputData.survey.value,
            onValueChange = {
                surveyInputData.survey.value = it
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
                        initCalendar {
                            surveyInputData.date.value = it
                            focusManager.clearFocus()
                        }
                    }
                },
            value = surveyInputData.date.value,
            onValueChange = {
                surveyInputData.date.value = it
            },
            readOnly = true,
            label = { Text(stringResource(R.string.health_diary_survey_date)) }
        )
        ExpandedDropdownMenu(
            label = stringResource(R.string.profile_caps),
            selectedTitle = surveyInputData.profile.value,
            expanded = profilesMenuExpanded.value,
            selectionOptions = profiles,
            onExpandedChange = {
                profilesMenuExpanded.value = !profilesMenuExpanded.value
            },
            onSelectedText = {
                surveyInputData.profile.value = it
            }
        )
        ExpandedDropdownMenu(
            label = stringResource(R.string.survey_change_survey_type),
            selectedTitle = surveyInputData.type.value,
            expanded = typesMenuExpanded.value,
            selectionOptions = types,
            onExpandedChange = {
                typesMenuExpanded.value = !typesMenuExpanded.value
            },
            onSelectedText = {
                surveyInputData.type.value = it
            }
        )
        if (surveyId != null) {
            DeleteButton(
                title = R.string.survey_delete,
                onClick = {
                    deleteSurvey(
                        title = surveyInputData.survey.value,
                        surveyId = surveyId
                    )
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

private fun deleteSurvey(title: String, surveyId: Long) {
    // todo
//    MaterialDialog(requireActivity()).show {
//        title(text = getString(R.string.survey_delete_explanation, title))
//        positiveButton {
//            vm.deleteSurvey(surveyId)
//            cancel()
//        }
//        negativeButton { cancel() }
//    }
}

private fun initCalendar(onClick: (String) -> Unit) {
    // todo
//    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
//    val calendar: Calendar = Calendar.getInstance()
//    val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
//        calendar.set(year, month, dayOfMonth)
//        onClick(dateFormat.format(calendar.time))
//    }
//
//    DatePickerDialog(
//        requireActivity(),
//        listener,
//        calendar.get(Calendar.YEAR),
//        calendar.get(Calendar.MONTH),
//        calendar.get(Calendar.DAY_OF_MONTH)
//    ).show()
}
