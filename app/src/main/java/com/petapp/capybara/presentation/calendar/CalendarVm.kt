package com.petapp.capybara.presentation.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.petapp.capybara.core.DataState
import com.petapp.capybara.core.navigation.IMainNavigator
import com.petapp.capybara.core.viewmodel.BaseViewModel
import com.petapp.capybara.core.viewmodel.SavedStateVmAssistedFactory
import com.petapp.capybara.data.IProfileRepository
import com.petapp.capybara.data.ISurveysRepository
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.presentation.filterSurveys
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
) : BaseViewModel() {

    private val _calendarState = MutableLiveData<DataState<CalendarUI>>()
    val calendarState: LiveData<DataState<CalendarUI>> get() = _calendarState

    init {
        getProfiles()
    }

    private fun getProfiles() {
        profileRepository.getProfiles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.isEmpty()) {
                        _calendarState.value = DataState.EMPTY
                    } else {
                        getSurveys(it)
                    }
                },
                {
                    _calendarState.value = DataState.ERROR(it)
                }
            ).connect()
    }

    private fun getSurveys(profiles: List<Profile>) {
        surveysRepository.getAllSurveys()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    val firstProfileId = profiles.first().id
                    _calendarState.value = DataState.DATA(
                        CalendarUI(
                            profiles = profiles,
                            surveys = it,
                            checkedProfileId = firstProfileId,
                            checkedSurveysDates = it.toDates(firstProfileId)
                        )
                    )
                },
                {
                    _calendarState.value = DataState.ERROR(it)
                }
            ).connect()
    }

    fun getCheckedSurveys(id: Long) {
        _calendarState.value?.onData {
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
