package com.petapp.capybara.survey

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.model.Survey
import com.petapp.capybara.core.data.model.Type
import com.petapp.capybara.core.data.repository.ProfileRepository
import com.petapp.capybara.core.data.repository.SurveysRepository
import com.petapp.capybara.core.data.repository.TypesRepository
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.core.mvi.SideEffect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SurveyVm(
    private val surveysRepository: SurveysRepository,
    private val typesRepository: TypesRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _surveyState = MutableStateFlow<DataState<SurveyMode>>(DataState.READY)
    val surveyState: StateFlow<DataState<SurveyMode>> get() = _surveyState.asStateFlow()

    private val _sideEffect = MutableStateFlow<SideEffect>(SideEffect.READY)
    val sideEffect: StateFlow<SideEffect> get() = _sideEffect.asStateFlow()

    fun getSurvey(survey: Survey?) {
        if (survey == null) {
            getProfilesTypes()
        } else {
            getSurveyProfilesTypes(survey.id)
        }
    }

    private fun getProfilesTypes() {
        viewModelScope.launch {
            runCatching {
                val profiles = profileRepository.getProfiles()
                val types = typesRepository.getTypes()
                SurveyNew(
                    profiles = profiles,
                    types = types
                )
            }
                .onSuccess {
                    _surveyState.value = DataState.DATA(SurveyMode.NEW(it))
                }
                .onFailure {
                    _surveyState.value = DataState.ERROR(it)
                }
        }
    }

    private fun getSurveyProfilesTypes(surveyId: Long) {
        viewModelScope.launch {
            runCatching {
                val survey = surveysRepository.getSurvey(surveyId)
                val profiles = profileRepository.getProfiles()
                val types = typesRepository.getTypes()
                val profileTitle = profiles.find { it.id == survey.profileId }?.name.orEmpty()
                val typeTitle = types.find { it.id == survey.typeId }?.name.orEmpty()
                SurveyUI(
                    survey = survey,
                    profileTitle = profileTitle,
                    typeTitle = typeTitle,
                    profiles = profiles,
                    types = types
                )
            }
                .onSuccess {
                    _surveyState.value = DataState.DATA(SurveyMode.READONLY(it))
                }
                .onFailure {
                    _surveyState.value = DataState.ERROR(it)
                }
        }
    }

    fun verifySurvey(
        mode: SurveyMode,
        surveyInputData: SurveyInputData
    ) {
        if (
            surveyInputData.survey.value.isEmpty() ||
            surveyInputData.date.value.isEmpty() ||
            surveyInputData.profile.value.isEmpty() ||
            surveyInputData.type.value.isEmpty()
        ) {
            _sideEffect.value = SideEffect.ACTION
        } else {
            val survey = createSurvey(mode, surveyInputData) ?: return

            viewModelScope.launch {
                runCatching {
                    if (mode is SurveyMode.NEW) {
                        surveysRepository.createSurvey(survey)
                    } else {
                        surveysRepository.updateSurvey(survey)
                    }
                }
                    .onSuccess {
                        // todo
                    }
                    .onFailure {
                        _surveyState.value = DataState.ERROR(it)
                    }
            }
        }
    }

    private fun createSurvey(
        mode: SurveyMode,
        surveyInputData: SurveyInputData
    ): Survey? {
        return when (mode) {
            is SurveyMode.EDIT -> {
                val profileTitle = surveyInputData.profile.value
                val typeTitle = surveyInputData.type.value
                val profile = mode.data.profiles.find { it.name == profileTitle }
                val type = mode.data.types.find { it.name == typeTitle }

                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
                val time = dateFormat.parse(surveyInputData.date.value)
                val monthYear = time?.let { SimpleDateFormat("LLLL yyyy", Locale("ru")).format(it).capitalize() }

                if (profile != null && type != null) {
                    Survey(
                        id = mode.data.survey.id,
                        name = surveyInputData.survey.value,
                        date = surveyInputData.date.value,
                        typeId = type.id,
                        typeIcon = type.icon,
                        profileId = profile.id,
                        profileIcon = profile.photo,
                        color = profile.color,
                        monthYear = monthYear.orEmpty()
                    )
                } else {
                    null
                }
            }
            is SurveyMode.NEW -> {
                val profileTitle = surveyInputData.profile.value
                val typeTitle = surveyInputData.type.value
                val profile = mode.data.profiles.find { it.name == profileTitle }
                val type = mode.data.types.find { it.name == typeTitle }

                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
                val time = dateFormat.parse(surveyInputData.date.value)
                val monthYear = time?.let { SimpleDateFormat("LLLL yyyy", Locale("ru")).format(it).capitalize() }

                if (profile != null && type != null) {
                    Survey(
                        id = 0L,
                        name = surveyInputData.survey.value,
                        date = surveyInputData.date.value,
                        typeId = type.id,
                        typeIcon = type.icon,
                        profileId = profile.id,
                        profileIcon = profile.photo,
                        color = profile.color,
                        monthYear = monthYear.orEmpty()
                    )
                } else {
                    null
                }
            }
            else -> null
        }
    }

    fun deleteSurvey(surveyId: Long) {
        viewModelScope.launch {
            runCatching {
                surveysRepository.deleteSurvey(surveyId)
            }
                .onSuccess {
                    // todo
                }
                .onFailure {
                    _surveyState.value = DataState.ERROR(it)
                }
        }
    }

    fun toEditMode(data: SurveyUI) {
        _surveyState.value = DataState.DATA(
            SurveyMode.EDIT(data)
        )
    }

    fun dismissSnackbar() {
        _sideEffect.value = SideEffect.READY
    }
}

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
