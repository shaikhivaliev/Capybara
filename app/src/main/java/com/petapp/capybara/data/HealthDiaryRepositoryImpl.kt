package com.petapp.capybara.data

import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary
import com.petapp.capybara.data.model.healthDiary.SurveyHealthDiary
import com.petapp.capybara.database.AppDao
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class HealthDiaryRepositoryImpl(private val appDao: AppDao) : HealthDiaryRepository {

    override fun getItemsHealthDiary(): Single<List<ItemHealthDiary>> {
        return appDao.getItemHealthDiaryWithSurveys().map { it.toHealthDiaryItems() }
    }

    override fun createSurveyHealthDiary(survey: SurveyHealthDiary): Completable {
        return Completable.fromAction { appDao.createHealthDiarySurvey(survey.toSurveyHealthDiaryEntity()) }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteSurveyHealthDiary(surveyId: String): Completable {
        return Completable.fromAction { appDao.deleteSurveyHealthDiary(surveyId) }
            .subscribeOn(Schedulers.io())
    }
}
