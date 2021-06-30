package com.petapp.capybara.presentation.survey

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.petapp.capybara.R
import com.petapp.capybara.core.navigation.IMainNavigator
import com.petapp.capybara.core.viewmodel.BaseViewModel
import com.petapp.capybara.core.viewmodel.SavedStateVmAssistedFactory
import com.petapp.capybara.data.IProfileRepository
import com.petapp.capybara.data.ISurveysRepository
import com.petapp.capybara.data.ITypesRepository
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.data.model.Type
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SurveyVmFactory(
    private val mainNavigator: IMainNavigator,
    private val surveysRepository: ISurveysRepository,
    private val typesRepository: ITypesRepository,
    private val profileRepository: IProfileRepository
) : SavedStateVmAssistedFactory<SurveyVm> {
    override fun create(handle: SavedStateHandle) =
        SurveyVm(
            savedStateHandle = handle,
            mainNavigator = mainNavigator,
            surveysRepository = surveysRepository,
            typesRepository = typesRepository,
            profileRepository = profileRepository
        )
}

class SurveyVm(
    private val savedStateHandle: SavedStateHandle,
    private val mainNavigator: IMainNavigator,
    private val surveysRepository: ISurveysRepository,
    private val typesRepository: ITypesRepository,
    private val profileRepository: IProfileRepository
) : BaseViewModel() {

    private val _types = MutableLiveData<List<Type>>()
    val types: LiveData<List<Type>> get() = _types

    private val _survey = MutableLiveData<Survey>()
    val survey: LiveData<Survey> get() = _survey

    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> get() = _profiles

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    init {
        getTypes()
        getProfiles()
    }

    private fun getTypes() {
        typesRepository.getTypes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _types.value = it
                Log.d(TAG, "get types success")
            },
                {
                    _errorMessage.value = R.string.error_get_types
                    Log.d(TAG, "get types error")
                }
            ).connect()
    }

    fun getSurvey(surveyId: Long) {
        surveysRepository.getSurvey(surveyId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _survey.value = it
                Log.d(TAG, "get survey ${it.id} success")
            },
                {
                    _errorMessage.value = R.string.error_get_survey
                    Log.d(TAG, "get survey error")
                }
            ).connect()
    }

    private fun getProfiles() {
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

    fun createSurvey(survey: Survey?) {
        if (survey != null) {
            surveysRepository.createSurvey(survey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    openTypesScreen()
                    Log.d(TAG, "create survey success")
                }, {
                    _errorMessage.value = R.string.error_create_survey
                    Log.d(TAG, "create survey error")
                }).connect()
        }
    }

    fun updateSurvey(survey: Survey?) {
        if (survey != null) {
            surveysRepository.updateSurvey(survey)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        openTypesScreen()
                        Log.d(TAG, "update survey ${survey.id} success")
                    },
                    {
                        _errorMessage.value = R.string.error_update_survey
                        Log.d(TAG, "update survey ${survey.id} error")
                    }
                ).connect()
        }
    }

    fun deleteSurvey(surveyId: Long) {
        surveysRepository.deleteSurvey(surveyId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    openTypesScreen()
                    Log.d(TAG, "delete survey $surveyId success")
                },
                {
                    _errorMessage.value = R.string.error_delete_survey
                    Log.d(TAG, "delete survey error")
                }
            ).connect()
    }

    private fun openTypesScreen() {
        mainNavigator.openTypes()
    }

    fun back() {
        mainNavigator.back()
    }

    companion object {
        private const val TAG = "database_survey"
    }
}
