package com.petapp.capybara.core.data.repository

import com.petapp.capybara.core.data.model.Survey
import com.petapp.capybara.core.data.toSurvey
import com.petapp.capybara.core.data.toSurveyEntity
import com.petapp.capybara.core.database.AppDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SurveysRepositoryImpl(
    private val appDao: AppDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SurveysRepository {

    override suspend fun getSurveysByType(typeId: Long): List<Survey> {
        return withContext(dispatcher) {
            appDao.getSurveysByType(typeId).map { it.toSurvey() }
        }
    }

    override suspend fun getAllSurveys(): List<Survey> {
        return withContext(dispatcher) {
            appDao.getAllSurveys().map { it.toSurvey() }
        }
    }

    override suspend fun getSurvey(surveyId: Long): Survey {
        return withContext(dispatcher) {
            appDao.getSurvey(surveyId).toSurvey()
        }
    }

    override suspend fun createSurvey(survey: Survey) {
        return withContext(dispatcher) {
            appDao.createSurvey(survey.toSurveyEntity())
        }
    }

    override suspend fun updateSurvey(survey: Survey) {
        return withContext(dispatcher) {
            appDao.updateSurvey(survey.toSurveyEntity())
        }
    }

    override suspend fun deleteSurvey(surveyId: Long) {
        return withContext(dispatcher) {
            appDao.deleteSurvey(surveyId)
        }
    }
}
