package com.petapp.capybara.data

import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.database.AppDao
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SurveysRepositoryImpl(private val appDao: AppDao) : SurveysRepository {

    override fun getSurveysByType(typeId: String): Single<List<Survey>> {
        return appDao.getSurveysByType(typeId).map { it.toSurveys() }
    }

    override fun getSurveysByMonth(month: String): Single<List<Survey>> {
        return appDao.getSurveysByMonth(month).map { it.toSurveys() }
    }

    override fun getSurvey(surveyId: String): Single<Survey> {
        return appDao.getSurvey(surveyId).map { it.toSurvey() }
    }

    override fun createSurvey(survey: Survey): Completable {
        return Completable.fromAction { appDao.createSurvey(survey.toSurveyEntity()) }
            .subscribeOn(Schedulers.io())
    }

    override fun updateSurvey(survey: Survey): Completable {
        return Completable.fromAction { appDao.updateSurvey(survey.toSurveyEntity()) }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteSurvey(surveyId: String): Completable {
        return Completable.fromAction { appDao.deleteSurvey(surveyId) }
            .subscribeOn(Schedulers.io())
    }
}
