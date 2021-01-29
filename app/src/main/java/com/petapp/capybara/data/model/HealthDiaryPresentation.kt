package com.petapp.capybara.data.model

import com.petapp.capybara.adapter.ListDiffer

sealed class HealthDiary : ListDiffer<HealthDiary> {

    override fun areItemsTheSame(other: HealthDiary) = when {
        this is ItemHealthDiary && other is ItemHealthDiary -> type == other.type
        this is SubItemHealthDiary && other is SubItemHealthDiary -> type == other.type
        this is EmptyItemHealthDiary && other is EmptyItemHealthDiary -> type == other.type
        else -> false
    }

    override fun areContentsTheSame(other: HealthDiary) = this == other
}

data class ItemHealthDiary(
    val id: Int,
    val type: HealthDiaryType,
    var isExpanded: Boolean = false,
    val subItems: List<SubItemHealthDiary> = emptyList(),
    val emptyItem: EmptyItemHealthDiary = EmptyItemHealthDiary(
        id = id,
        type = type
    )
) : HealthDiary()

data class SubItemHealthDiary(
    val id: Int,
    val type: HealthDiaryType
) : HealthDiary()

data class EmptyItemHealthDiary(
    val id: Int,
    val type: HealthDiaryType
) : HealthDiary()
