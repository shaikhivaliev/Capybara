package com.petapp.capybara.surveys.domain

import com.petapp.capybara.surveys.domain.dto.Survey
import io.reactivex.Completable
import io.reactivex.Single

interface SurveysRepository {

    fun getSurveys(typeId: String): Single<List<Survey>>

    fun getSurvey(surveyId: String): Single<Survey>

    fun createSurvey(survey: Survey): Completable

    fun updateSurvey(survey: Survey): Completable

    fun deleteSurvey(surveyId: String): Completable

}