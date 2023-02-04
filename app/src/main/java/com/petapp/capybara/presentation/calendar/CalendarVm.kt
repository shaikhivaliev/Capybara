package com.petapp.capybara.presentation.calendar

import androidx.lifecycle.*
import com.petapp.capybara.core.mvi.DataState
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarVmFactory(
    private val mainNavigator: IMainNavigator,
    private val profileRepository: IProfileRepository,
    private val surveysRepository: ISurveysRepository
) : SavedStateVmAssistedFactory<CalendarVm> {
    override fun create(handle: SavedStateHandle) =
        CalendarVm(
            savedStateHandle = handle,
            mainNavigator = mainNavigator,
            profileRepository = profileRepository,
            surveysRepository = surveysRepository
        )
}

class CalendarVm(
    private val savedStateHandle: SavedStateHandle,
    private val mainNavigator: IMainNavigator,
    private val profileRepository: IProfileRepository,
    private val surveysRepository: ISurveysRepository
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

    fun openNewSurveyScreen() {
        mainNavigator.openNewSurvey()
    }

    fun openProfileScreen() {
        mainNavigator.openProfiles()
    }

    private fun List<Survey>.toDates(id: Long): List<LocalDate> {
        val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)
        return this
            .filterSurveys(id)
            .map {
                LocalDate.parse(it.date, dateFormat)
            }
    }
}

data class CalendarUI(
    val profiles: List<Profile>,
    val surveys: List<Survey>,
    val checkedProfileId: Long,
    val checkedSurveysDates: List<LocalDate>,
)
