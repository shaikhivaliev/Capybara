package com.petapp.capybara.core.data.repository

import com.petapp.capybara.core.data.toHealthDiaryItem
import com.petapp.capybara.core.data.toSurveyHealthDiaryEntity
import com.petapp.capybara.core.database.AppDao
import com.petapp.capybara.core.data.model.ItemHealthDiary
import com.petapp.capybara.core.data.model.SurveyHealthDiary
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HealthDiaryRepositoryImpl(
    private val appDao: AppDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : HealthDiaryRepository {

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
