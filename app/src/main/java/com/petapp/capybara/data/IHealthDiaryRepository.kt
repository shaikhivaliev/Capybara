package com.petapp.capybara.data

import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary
import com.petapp.capybara.data.model.healthDiary.SurveyHealthDiary

interface IHealthDiaryRepository {

    suspend fun getItemsHealthDiary(): List<ItemHealthDiary>

    suspend fun createSurveyHealthDiary(survey: SurveyHealthDiary)

    suspend fun deleteSurveyHealthDiary(surveyId: Long)
}
