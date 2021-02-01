package com.petapp.capybara.data.model

import com.petapp.capybara.adapter.ListDiffer

sealed class HealthDiary : ListDiffer<HealthDiary> {

    override fun areItemsTheSame(other: HealthDiary) = when {
        this is ItemHealthDiary && other is ItemHealthDiary -> type == other.type
        this is SurveyHealthDiary && other is SurveyHealthDiary -> type == other.type
        this is EmptyItemHealthDiary && other is EmptyItemHealthDiary -> type == other.type
        else -> false
    }

    override fun areContentsTheSame(other: HealthDiary) = this == other
}

data class ItemHealthDiary(
    val id: Int,
    val type: HealthDiaryType,
    var isExpanded: Boolean = false,
    val surveys: List<SurveyHealthDiary> = emptyList(),
    val emptyItem: EmptyItemHealthDiary = EmptyItemHealthDiary(
        id = id,
        type = type
    )
) : HealthDiary()

data class SurveyHealthDiary(
    val id: String,
    val type: HealthDiaryType,
    val date: String,
    val time: String,
    val surveyValue: String,
    val unitOfMeasure: String
) : HealthDiary()

data class EmptyItemHealthDiary(
    val id: Int,
    val type: HealthDiaryType
) : HealthDiary()
