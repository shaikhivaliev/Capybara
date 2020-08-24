package com.petapp.capybara.data

import com.petapp.capybara.data.model.Survey
import io.reactivex.Completable
import io.reactivex.Single

interface SurveysRepository {

    fun getSurveys(typeId: String): Single<List<Survey>>

    fun getSurvey(surveyId: String): Single<Survey>

    fun createSurvey(survey: Survey): Completable

    fun updateSurvey(survey: Survey): Completable

    fun deleteSurvey(surveyId: String): Completable
}