package com.petapp.capybara.data

import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.database.AppDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SurveysRepository(
    private val appDao: AppDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ISurveysRepository {

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
