package com.petapp.capybara.presentation.surveys

import androidx.lifecycle.*
import com.petapp.capybara.core.state.DataState
import com.petapp.capybara.core.navigation.IMainNavigator
import com.petapp.capybara.core.viewmodel.SavedStateVmAssistedFactory
import com.petapp.capybara.data.IProfileRepository
import com.petapp.capybara.data.ISurveysRepository
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.presentation.filterSurveys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SurveysVmFactory(
    private val mainNavigator: IMainNavigator,
    private val surveysRepository: ISurveysRepository,
    private val profileRepository: IProfileRepository
) : SavedStateVmAssistedFactory<SurveysVm> {
    override fun create(handle: SavedStateHandle) =
        SurveysVm(
            savedStateHandle = handle,
            mainNavigator = mainNavigator,
            surveysRepository = surveysRepository,
            profileRepository = profileRepository
        )
}

class SurveysVm(
    private val savedStateHandle: SavedStateHandle,
    private val mainNavigator: IMainNavigator,
    private val surveysRepository: ISurveysRepository,
    private val profileRepository: IProfileRepository
) : ViewModel() {

    private val _surveysState = MutableStateFlow<DataState<SurveysState>>(DataState.READY)
    val surveysState: StateFlow<DataState<SurveysState>> get() = _surveysState.asStateFlow()

    fun getProfiles(typeId: Long?) {
        if (typeId == null) {
            _surveysState.value = DataState.ERROR()
            return
        }
        _surveysState.value = DataState.READY
        viewModelScope.launch {
            runCatching {
                profileRepository.getProfiles()
            }
                .onSuccess {
                    if (it.isEmpty()) {
                        _surveysState.value = DataState.EMPTY
                    } else {
                        getSurveys(
                            typeId = typeId,
                            profiles = it
                        )
                    }
                }
                .onFailure {
                    _surveysState.value = DataState.ERROR(it)
                }
        }
    }

    private fun getSurveys(typeId: Long, profiles: List<Profile>) {
        viewModelScope.launch {
            runCatching {
                surveysRepository.getSurveysByType(typeId)
            }
                .onSuccess {
                    val firstProfileId = profiles.first().id
                    _surveysState.value = DataState.DATA(
                        SurveysState(
                            profiles = profiles,
                            surveys = it,
                            checkedProfileId = firstProfileId,
                            checkedSurveys = it.filterSurveys(firstProfileId)
                        )
                    )
                }
                .onFailure {
                    _surveysState.value = DataState.ERROR(it)
                }
        }
    }

    fun getCheckedSurveys(id: Long) {
        _surveysState.value.onData {
            _surveysState.value = DataState.DATA(
                SurveysState(
                    profiles = it.profiles,
                    surveys = it.surveys,
                    checkedProfileId = id,
                    checkedSurveys = it.surveys.filterSurveys(id)
                )
            )
        }
    }

    fun openSurveyScreen(survey: Survey) {
        mainNavigator.openSurvey(survey)
    }

    fun openNewSurveyScreen() {
        mainNavigator.openNewSurvey()
    }

    fun openProfileScreen() {
        mainNavigator.openProfiles()
    }
}

data class SurveysState(
    val profiles: List<Profile>,
    val surveys: List<Survey>,
    val checkedProfileId: Long,
    val checkedSurveys: List<Survey>,
)
