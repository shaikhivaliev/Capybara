package com.petapp.capybara.surveys.data

import com.petapp.capybara.database.AppDao
import com.petapp.capybara.surveys.data.mappers.SurveyEntityMapper
import com.petapp.capybara.surveys.domain.SurveysRepository
import com.petapp.capybara.surveys.domain.dto.Survey
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SurveysDataRepository(
    private val appDao: AppDao,
    private val mapper: SurveyEntityMapper
) : SurveysRepository {

    override fun getSurveys(typeId: String): Single<List<Survey>> {
        return appDao.getSurveys(typeId).map(mapper::transformToSurvey)
    }

    override fun getSurvey(surveyId: String): Single<Survey> {
        return appDao.getSurvey(surveyId).map(mapper::transformToSurvey)
    }

    override fun createSurvey(survey: Survey): Completable {
        return Completable.fromAction { appDao.createSurvey(mapper.transformToSurveyEntity(survey)) }
            .subscribeOn(Schedulers.io())
    }

    override fun updateSurvey(survey: Survey): Completable {
        return Completable.fromAction { appDao.updateSurvey(mapper.transformToSurveyEntity(survey)) }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteSurvey(surveyId: String): Completable {
        return Completable.fromAction { appDao.deleteSurvey(surveyId) }
            .subscribeOn(Schedulers.io())
    }
}
