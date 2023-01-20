package com.petapp.capybara.data

import com.petapp.capybara.data.model.Survey

interface ISurveysRepository {

    suspend fun getSurveysByType(typeId: Long): List<Survey>

    suspend fun getAllSurveys(): List<Survey>

    suspend fun getSurvey(surveyId: Long): Survey

    suspend fun createSurvey(survey: Survey)

    suspend fun updateSurvey(survey: Survey)

    suspend fun deleteSurvey(surveyId: Long)
}
