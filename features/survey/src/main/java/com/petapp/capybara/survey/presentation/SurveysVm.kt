package com.petapp.capybara.survey.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.repository.ProfileRepository
import com.petapp.capybara.core.data.repository.SurveysRepository
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.survey.filterSurveys
import com.petapp.capybara.survey.state.SurveysState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SurveysVm(
    private val surveysRepository: SurveysRepository,
    private val profileRepository: ProfileRepository
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
}
