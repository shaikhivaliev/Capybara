package com.petapp.capybara.data.model.healthDiary

data class ItemHealthDiary(
    val id: Long,
    val type: HealthDiaryType,
    var isExpanded: Boolean = false,
    var surveys: List<SurveyHealthDiary> = emptyList(),
    val emptyItem: EmptyItemHealthDiary = EmptyItemHealthDiary(
        id = id,
        type = type
    )
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

data class EmptyItemHealthDiary(
    val id: Long,
    val type: HealthDiaryType
)
