package com.petapp.capybara.survey.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.model.Survey
import com.petapp.capybara.core.data.model.Type
import com.petapp.capybara.core.mvi.SideEffect

data class SurveysState(
    val profiles: List<Profile>,
    val surveys: List<Survey>,
    val checkedProfileId: Long,
    val checkedSurveys: List<Survey>,
)

sealed class SurveyMode {
    data class NEW(val data: SurveyNew) : SurveyMode()
    data class EDIT(val data: SurveyUI) : SurveyMode()
    data class READONLY(val data: SurveyUI) : SurveyMode()
}

data class SurveyNew(
    val profiles: List<Profile>,
    val types: List<Type>
)

data class SurveyUI(
    val survey: Survey,
    val profileTitle: String,
    val typeTitle: String,
    val profiles: List<Profile>,
    val types: List<Type>
)

data class SurveyInputData(
    val survey: MutableState<String> = mutableStateOf(""),
    val date: MutableState<String> = mutableStateOf(""),
    val profile: MutableState<String> = mutableStateOf(""),
    val type: MutableState<String> = mutableStateOf("")
)

sealed class SurveyEffect : SideEffect {
    object Ready : SurveyEffect()
    object NavigateToType : SurveyEffect()
    object ShowSnackbar : SurveyEffect()
}
