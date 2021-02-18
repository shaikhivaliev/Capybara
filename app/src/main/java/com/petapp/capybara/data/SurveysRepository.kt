package com.petapp.capybara.data

import com.petapp.capybara.data.model.Survey
import io.reactivex.Completable
import io.reactivex.Single

interface SurveysRepository {

    fun getSurveysByType(typeId: Long): Single<List<Survey>>

    fun getSurveysByMonth(month: String?): Single<List<Survey>>

    fun getSurvey(surveyId: Long): Single<Survey>

    fun createSurvey(survey: Survey): Completable

    fun updateSurvey(survey: Survey): Completable

    fun deleteSurvey(surveyId: Long): Completable
}
