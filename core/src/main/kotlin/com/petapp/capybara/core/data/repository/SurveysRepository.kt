package com.petapp.capybara.core.data.repository

import com.petapp.capybara.core.data.model.Survey

interface SurveysRepository {

    suspend fun getSurveysByType(typeId: Long): List<Survey>

    suspend fun getAllSurveys(): List<Survey>

    suspend fun getSurvey(surveyId: Long): Survey

    suspend fun createSurvey(survey: Survey)

    suspend fun updateSurvey(survey: Survey)

    suspend fun deleteSurvey(surveyId: Long)
}
