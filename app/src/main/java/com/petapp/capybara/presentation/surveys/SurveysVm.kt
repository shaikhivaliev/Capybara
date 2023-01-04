package com.petapp.capybara.presentation.surveys

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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
) : BaseViewModel() {

    private val _surveysState = MutableLiveData<DataState<SurveysState>>()
    val surveysState: LiveData<DataState<SurveysState>> get() = _surveysState

    fun getMarks(typeId: Long?) {
        if (typeId == null) {
            _surveysState.value = DataState.ERROR()
            return
        }
        _surveysState.value = DataState.READY
        profileRepository.getProfiles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.isEmpty()) {
                        _surveysState.value = DataState.EMPTY
                    } else {
                        getSurveys(
                            typeId = typeId,
                            profiles = it
                        )
                    }
                },
                {
                    _surveysState.value = DataState.ERROR(it)
                }
            ).connect()
    }

    private fun getSurveys(typeId: Long, profiles: List<Profile>) {
        surveysRepository.getSurveysByType(typeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    val firstProfileId = profiles.first().id
                    _surveysState.value = DataState.DATA(
                        SurveysState(
                            profiles = profiles,
                            surveys = it,
                            checkedProfileId = firstProfileId,
                            checkedSurveys = it.filterSurveys(firstProfileId)
                        )
                    )
                },
                {
                    _surveysState.value = DataState.ERROR(it)
                }
            ).connect()
    }

    fun getCheckedSurveys(id: Long) {
        _surveysState.value?.onData {
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

    private fun List<Survey>.filterSurveys(id: Long): List<Survey> {
        return this.filter { item -> item.profileId == id }
    }
}

data class SurveysState(
    val profiles: List<Profile>,
    val surveys: List<Survey>,
    val checkedProfileId: Long,
    val checkedSurveys: List<Survey>,
)
