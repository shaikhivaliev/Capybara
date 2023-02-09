package com.petapp.capybara.core.data.model

import java.util.Collections.emptyList

data class ItemHealthDiary(
    val id: Long,
    val type: HealthDiaryType,
    val isExpanded: Boolean = false,
    var surveys: List<SurveyHealthDiary> = emptyList()
)

data class SurveyHealthDiary(
    val id: Long,
    val type: HealthDiaryType,
    val profileId: Long,
    val date: String,
    val time: String,
    val surveyValue: String,
    val unitOfMeasure: String
)
