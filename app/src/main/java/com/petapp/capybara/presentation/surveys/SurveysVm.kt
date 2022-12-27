package com.petapp.capybara.presentation.surveys

import android.util.Log
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

    fun getMarks(typeId: Long) {
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
                    val checkedSurveys = it.filter { item -> item.profileId == profiles.first().id }
                    _surveysState.value = DataState.DATA(
                        SurveysState(
                            profiles = profiles,
                            surveys = it,
                            checkedProfile = profiles.first(),
                            checkedSurveys = checkedSurveys
                        )
                    )
                },
                {
                    _surveysState.value = DataState.ERROR(it)
                }
            ).connect()
    }

    fun getCheckedSurveys(id: Long) {
        // todo
    }

    fun openSurveyScreen(survey: Survey?) {
        mainNavigator.openSurvey(survey)
    }

    fun openProfileScreen() {
        mainNavigator.openProfiles()
    }
}

data class SurveysState(
    val profiles: List<Profile>,
    val surveys: List<Survey>,
    val checkedProfile: Profile,
    val checkedSurveys: List<Survey>,
)
