package com.petapp.capybara.data

import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary
import com.petapp.capybara.data.model.healthDiary.SurveyHealthDiary
import com.petapp.capybara.database.AppDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HealthDiaryRepository(
    private val appDao: AppDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IHealthDiaryRepository {

    override suspend fun getItemsHealthDiary(): List<ItemHealthDiary> {
        return withContext(dispatcher) {
            appDao.getItemHealthDiaryWithSurveys().map {
                it.toHealthDiaryItem()
            }
        }
    }

    override suspend fun createSurveyHealthDiary(survey: SurveyHealthDiary) {
        return withContext(dispatcher) {
            appDao.createHealthDiarySurvey(survey.toSurveyHealthDiaryEntity())
        }
    }

    override suspend fun deleteSurveyHealthDiary(surveyId: Long) {
        return withContext(dispatcher) {
            appDao.deleteSurveyHealthDiary(surveyId)
        }
    }
}
