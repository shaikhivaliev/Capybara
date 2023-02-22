package com.petapp.capybara.calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petapp.capybara.calendar.state.CalendarUI
import com.petapp.capybara.calendar.toDates
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.repository.ProfileRepository
import com.petapp.capybara.core.data.repository.SurveysRepository
import com.petapp.capybara.core.mvi.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalendarVm(
    private val profileRepository: ProfileRepository,
    private val surveysRepository: SurveysRepository
) : ViewModel() {

    private val _calendarState = MutableStateFlow<DataState<CalendarUI>>(DataState.READY)
    val calendarState: StateFlow<DataState<CalendarUI>> get() = _calendarState.asStateFlow()

    init {
        getProfiles()
    }

    private fun getProfiles() {
        viewModelScope.launch {
            runCatching {
                profileRepository.getProfiles()
            }
                .onSuccess {
                    if (it.isEmpty()) {
                        _calendarState.value = DataState.EMPTY
                    } else {
                        getSurveys(it)
                    }
                }
                .onFailure {
                    _calendarState.value = DataState.ERROR(it)
                }
        }
    }

    private fun getSurveys(profiles: List<Profile>) {
        viewModelScope.launch {
            runCatching {
                surveysRepository.getAllSurveys()
            }
                .onSuccess {
                    val firstProfileId = profiles.first().id
                    _calendarState.value = DataState.DATA(
                        CalendarUI(
                            profiles = profiles,
                            surveys = it,
                            checkedProfileId = firstProfileId,
                            checkedSurveysDates = it.toDates(firstProfileId)
                        )
                    )
                }
                .onFailure {
                    _calendarState.value = DataState.ERROR(it)
                }
        }
    }

    fun getCheckedSurveys(id: Long) {
        _calendarState.value.onData {
            _calendarState.value = DataState.DATA(
                CalendarUI(
                    profiles = it.profiles,
                    surveys = it.surveys,
                    checkedProfileId = id,
                    checkedSurveysDates = it.surveys.toDates(id)
                )
            )
        }
    }
}
