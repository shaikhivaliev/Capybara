package com.petapp.capybara.presentation.survey

import androidx.compose.runtime.MutableState
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
import io.reactivex.Single
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

    fun getSurvey(survey: Survey?) {
        if (survey == null) {
            getProfilesTypes()
        } else {
            getSurveyProfilesTypes(survey.id)
        }
    }

    private fun getProfilesTypes() {
        Single.zip(
            profileRepository.getProfiles()
                .subscribeOn(Schedulers.io()),
            typesRepository.getTypes()
                .subscribeOn(Schedulers.io())
        ) { profiles, types ->
            SurveyNew(
                profiles = profiles,
                types = types
            )
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _surveyState.value = DataState.DATA(SurveyMode.NEW(it))
            }, {
                _surveyState.value = DataState.ERROR(it)
            }).connect()

    }

    private fun getSurveyProfilesTypes(surveyId: Long) {
        Single.zip(
            surveysRepository.getSurvey(surveyId)
                .subscribeOn(Schedulers.io()),
            profileRepository.getProfiles()
                .subscribeOn(Schedulers.io()),
            typesRepository.getTypes()
                .subscribeOn(Schedulers.io())
        ) { survey, profiles, types ->
            val profileTitle = profiles.find { it.id == survey.profileId }?.name.orEmpty()
            val typeTitle = types.find { it.id == survey.typeId }?.name.orEmpty()
            SurveyUI(
                survey = survey,
                profileTitle = profileTitle,
                typeTitle = typeTitle,
                profiles = profiles,
                types = types
            )
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _surveyState.value = DataState.DATA(SurveyMode.READONLY(it))
            }, {
                _surveyState.value = DataState.ERROR(it)
            }).connect()
    }

    fun verifySurvey(
        value: Survey?,
        surveyTitle: MutableState<String>,
        date: MutableState<String>,
        profileTitle: MutableState<String>,
        typeTitle: MutableState<String>
    ) {
//        surveysRepository.updateSurvey(survey)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                openTypesScreen()
//            }, {
//                _surveyState.value = DataState.ERROR(it)
//            }).connect()
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

    fun toEditMode(data: SurveyUI) {
        _surveyState.value = DataState.DATA(
            SurveyMode.EDIT(data)
        )
    }
}

sealed class SurveyMode {
    data class NEW(val data: SurveyNew) : SurveyMode()
    data class EDIT(val data: SurveyUI) : SurveyMode()
    data class READONLY(val data: SurveyUI) : SurveyMode()
}

data class SurveyNew(
    val profiles: List<Profile>,
    val types: List<Type>
)

data class SurveyUI(
    val survey: Survey,
    val profileTitle: String,
    val typeTitle: String,
    val profiles: List<Profile>,
    val types: List<Type>
)
