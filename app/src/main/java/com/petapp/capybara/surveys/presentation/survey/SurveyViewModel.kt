package com.petapp.capybara.surveys.presentation.survey

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.petapp.capybara.common.BaseViewModel
import com.petapp.capybara.surveys.domain.SurveysRepository
import com.petapp.capybara.surveys.domain.dto.Survey
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SurveyViewModel(
    private val repository: SurveysRepository
) : BaseViewModel() {

    val survey = MutableLiveData<Survey>()
    val isChangeDone = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun getSurvey(surveyId: String) {
        repository.getSurvey(surveyId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                survey.value = it
                Log.d("database", "get survey ${it.id} success")
            },
                {
                    errorMessage.value = "error"
                    Log.d("database", "get survey error")
                }
            ).connect()
    }

    fun createSurvey(survey: Survey) {
        repository.createSurvey(survey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isChangeDone.value = true
                Log.d("database", "create survey success")
            }, {
                errorMessage.value = "error"
                Log.d("database", "create survey error")
            }).connect()
    }

    fun updateSurvey(survey: Survey) {
        repository.updateSurvey(survey)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    isChangeDone.value = true
                    Log.d("database", "update survey ${survey.id} success")
                },
                {
                    isChangeDone.value = false
                    errorMessage.value = "error"
                    Log.d("database", "update survey ${survey.id} error")
                }
            ).connect()
    }

    fun deleteSurvey(surveyId: String) {
        repository.deleteSurvey(surveyId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    isChangeDone.value = true
                    Log.d("database", "delete survey $surveyId success")
                },
                {
                    errorMessage.value = "error"
                    Log.d("database", "delete survey error")
                }
            ).connect()
    }
}