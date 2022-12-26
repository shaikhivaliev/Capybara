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

    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> get() = _profiles

    private val _surveysState = MutableLiveData<DataState<List<Survey>>>()
    val surveysState: LiveData<DataState<List<Survey>>> get() = _surveysState

    val profileId = MutableLiveData<Long>()

    init {
        getMarks()
    }

    private fun getMarks() {
        profileRepository.getProfiles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _profiles.value = it
                },
                {
                    _surveysState.value = DataState.ERROR(it)
                }
            ).connect()
    }

    fun getSurveys(typeId: Long) {
        surveysRepository.getSurveysByType(typeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    val surveys = it.filter { item -> item.profileId == profileId.value }
                    if (surveys.isEmpty()) {
                        _surveysState.value = DataState.EMPTY
                    } else {
                        _surveysState.value = DataState.DATA(surveys)
                    }
                },
                {
                    _surveysState.value = DataState.ERROR(it)
                }
            ).connect()
    }

    fun openSurveyScreen(survey: Survey?) {
        mainNavigator.openSurvey(survey)
    }

    fun openProfileScreen() {
        mainNavigator.openProfiles()
    }
}
