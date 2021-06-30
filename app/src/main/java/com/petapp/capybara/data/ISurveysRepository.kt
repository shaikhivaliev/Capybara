package com.petapp.capybara.data

import com.petapp.capybara.data.model.Months
import com.petapp.capybara.data.model.Survey
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

interface ISurveysRepository {

    fun getSurveysByType(typeId: Long): Single<List<Survey>>

    fun getInitMonths(currentDate: Calendar): Single<Months>

    fun getSurveysByMonth(currentDate: Calendar): Single<List<Survey>>

    fun getSurvey(surveyId: Long): Single<Survey>

    fun createSurvey(survey: Survey): Completable

    fun updateSurvey(survey: Survey): Completable

    fun deleteSurvey(surveyId: Long): Completable
}
