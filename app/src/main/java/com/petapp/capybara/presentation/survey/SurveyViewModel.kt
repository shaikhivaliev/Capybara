package com.petapp.capybara.presentation.survey

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.petapp.capybara.common.BaseViewModel
import com.petapp.capybara.R
import com.petapp.capybara.data.MarksRepository
import com.petapp.capybara.data.SurveysRepository
import com.petapp.capybara.data.TypesRepository
import com.petapp.capybara.data.model.Mark
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.extensions.navigateWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SurveyViewModel(
    private val repositorySurveys: SurveysRepository,
    private val repositoryTypes: TypesRepository,
    private val repositoryMarks: MarksRepository,
    private val navController: NavController
) : BaseViewModel() {

    private val _types = MutableLiveData<List<Type>>()
    val types: LiveData<List<Type>> get() = _types

    private val _survey = MutableLiveData<Survey>()
    val survey: LiveData<Survey> get() = _survey

    private val _marks = MutableLiveData<List<Mark>>()
    val marks: LiveData<List<Mark>> get() = _marks

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    init {
        getTypes()
        getMarks()
    }

    private fun getTypes() {
        repositoryTypes.getTypes()
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

    fun getSurvey(surveyId: String) {
        repositorySurveys.getSurvey(surveyId)
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

    private fun getMarks() {
        repositoryMarks.getMarks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _marks.value = it
                    Log.d(TAG, "get marks success")
                },
                {
                    _errorMessage.value = R.string.error_get_marks
                    Log.d(TAG, "get marks error")
                }
            ).connect()
    }

    fun createSurvey(survey: Survey?) {
        if (survey != null) {
            repositorySurveys.createSurvey(survey)
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
            repositorySurveys.updateSurvey(survey)
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

    fun deleteSurvey(surveyId: String) {
        repositorySurveys.deleteSurvey(surveyId)
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
        SurveyFragmentDirections.toTypes().navigateWith(navController)
    }

    fun back() {
        navController.popBackStack()
    }

    companion object {
        private const val TAG = "database_survey"
    }
}
