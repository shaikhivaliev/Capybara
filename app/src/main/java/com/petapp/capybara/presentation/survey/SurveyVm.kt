package com.petapp.capybara.presentation.survey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.petapp.capybara.core.DataState
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
    override fun create(handle: SavedStateHandle) = SurveyVm(
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

    private val _surveyState = MutableLiveData<DataState<SurveyMode>>()
    val surveyState: LiveData<DataState<SurveyMode>> get() = _surveyState

    private val _types = MutableLiveData<List<Type>>()
    val types: LiveData<List<Type>> get() = _types

    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> get() = _profiles

    fun getSurvey(survey: Survey?) {
        if (survey == null) {
            _surveyState.value = DataState.DATA(SurveyMode.NEW)
        } else {
            surveysRepository.getSurvey(survey.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _surveyState.value = DataState.DATA(SurveyMode.OPEN(it))
                }, {
                    _surveyState.value = DataState.ERROR(it)
                }).connect()
        }
    }

    fun createSurvey(survey: Survey) {
        surveysRepository.createSurvey(survey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                openTypesScreen()
            }, {
                _surveyState.value = DataState.ERROR(it)
            }).connect()
    }

    fun updateSurvey(survey: Survey) {
        surveysRepository.updateSurvey(survey)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                openTypesScreen()
            }, {
                _surveyState.value = DataState.ERROR(it)
            }).connect()
    }

    fun deleteSurvey(surveyId: Long) {
        surveysRepository.deleteSurvey(surveyId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                openTypesScreen()
            }, {
                _surveyState.value = DataState.ERROR(it)
            }).connect()
    }

    private fun openTypesScreen() {
        mainNavigator.openTypes()
    }

    fun toEditMode(survey: Survey) {
        _surveyState.value = DataState.DATA(SurveyMode.EDIT(survey))
    }


    private fun getTypes() {
        typesRepository.getTypes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _types.value = it
            }, {
                _surveyState.value = DataState.ERROR(it)
            }).connect()
    }


    private fun getProfiles() {
        profileRepository.getProfiles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _profiles.value = it
            }, {
                _surveyState.value = DataState.ERROR(it)
            }).connect()
    }
}

sealed class SurveyMode {
    object NEW : SurveyMode()
    data class EDIT(val survey: Survey) : SurveyMode()
    data class OPEN(val survey: Survey) : SurveyMode()
}
