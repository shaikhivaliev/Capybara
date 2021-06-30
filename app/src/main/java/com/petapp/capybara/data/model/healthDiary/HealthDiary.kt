package com.petapp.capybara.data.model.healthDiary

import com.petapp.capybara.core.list.ListItem

sealed class HealthDiary : ListItem

data class ItemHealthDiary(
    override val id: Long,
    val type: HealthDiaryType,
    var isExpanded: Boolean = false,
    var surveys: List<SurveyHealthDiary> = emptyList(),
    val emptyItem: EmptyItemHealthDiary = EmptyItemHealthDiary(
        id = id,
        type = type
    )
) : HealthDiary()

data class SurveyHealthDiary(
    override val id: Long,
    val type: HealthDiaryType,
    val profileId: Long,
    val date: String,
    val time: String,
    val surveyValue: String,
    val unitOfMeasure: String
) : HealthDiary()

data class EmptyItemHealthDiary(
    override val id: Long,
    val type: HealthDiaryType
) : HealthDiary()
