package com.petapp.capybara.data

import com.petapp.capybara.data.model.Months
import com.petapp.capybara.data.model.Survey
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

interface SurveysRepository {

    fun getSurveysByType(typeId: Long): Single<List<Survey>>

    fun getSurveysByMonths(currentDate: Calendar): Single<Months>

    fun getSurvey(surveyId: Long): Single<Survey>

    fun createSurvey(survey: Survey): Completable

    fun updateSurvey(survey: Survey): Completable

    fun deleteSurvey(surveyId: Long): Completable
}
