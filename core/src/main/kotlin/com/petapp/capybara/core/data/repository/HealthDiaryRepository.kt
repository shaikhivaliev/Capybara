package com.petapp.capybara.core.data.repository

import com.petapp.capybara.core.data.model.ItemHealthDiary
import com.petapp.capybara.core.data.model.SurveyHealthDiary

interface HealthDiaryRepository {

    suspend fun getItemsHealthDiary(): List<ItemHealthDiary>

    suspend fun createSurveyHealthDiary(survey: SurveyHealthDiary)

    suspend fun deleteSurveyHealthDiary(surveyId: Long)
}
