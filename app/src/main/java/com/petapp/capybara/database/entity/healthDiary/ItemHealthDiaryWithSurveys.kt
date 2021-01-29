package com.petapp.capybara.database.entity.healthDiary

import androidx.room.Embedded
import androidx.room.Relation

data class ItemHealthDiaryWithSurveys(
    @Embedded val item: ItemHealthDiaryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "item_id"
    )
    val surveys: List<SurveyHealthDiaryEntity>
)
