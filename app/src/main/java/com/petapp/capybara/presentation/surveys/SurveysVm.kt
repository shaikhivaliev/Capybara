package com.petapp.capybara.presentation.surveys

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.petapp.capybara.R
import com.petapp.capybara.core.navigation.IMainCoordinator
import com.petapp.capybara.core.viewmodel.BaseViewModel
import com.petapp.capybara.core.viewmodel.SavedStateVmAssistedFactory
import com.petapp.capybara.data.IProfileRepository
import com.petapp.capybara.data.ISurveysRepository
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SurveysVmFactory(
    private val mainNavigator: IMainCoordinator,
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
    private val mainNavigator: IMainCoordinator,
    private val surveysRepository: ISurveysRepository,
    private val profileRepository: IProfileRepository
) : BaseViewModel() {

    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> get() = _profiles

    private val _surveys = MutableLiveData<List<Survey>>()
    val surveys: LiveData<List<Survey>> get() = _surveys

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

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
                    Log.d(TAG, "get profiles success")
                },
                {
                    _errorMessage.value = R.string.error_get_marks
                    Log.d(TAG, "get profiles error")
                }
            ).connect()
    }

    fun getSurveys(typeId: Long) {
        surveysRepository.getSurveysByType(typeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _surveys.value = it.filter { item -> item.profileId == profileId.value }
                    Log.d(TAG, "get surveys success")
                },
                {
                    _errorMessage.value = R.string.error_get_surveys
                    Log.d(TAG, "get profiles error")
                }
            ).connect()
    }

    fun openSurveyScreen(survey: Survey?) {
        mainNavigator.openSurvey(survey)
    }

    fun openProfileScreen() {
        mainNavigator.openProfiles()
    }

    companion object {
        private const val TAG = "database_surveys"
    }
}
