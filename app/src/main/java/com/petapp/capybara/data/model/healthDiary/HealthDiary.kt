package com.petapp.capybara.data.model.healthDiary

import com.petapp.capybara.common.ListItem

sealed class HealthDiary : ListItem

data class ItemHealthDiary(
    val id: Int,
    val type: HealthDiaryType,
    var isExpanded: Boolean = false,
    var surveys: List<SurveyHealthDiary> = emptyList(),
    val emptyItem: EmptyItemHealthDiary = EmptyItemHealthDiary(
        id = id,
        type = type
    )
) : HealthDiary()

data class SurveyHealthDiary(
    val id: String,
    val type: HealthDiaryType,
    val profileId: String,
    val date: String,
    val time: String,
    val surveyValue: String,
    val unitOfMeasure: String
) : HealthDiary()

data class EmptyItemHealthDiary(
    val id: Int,
    val type: HealthDiaryType
) : HealthDiary()
