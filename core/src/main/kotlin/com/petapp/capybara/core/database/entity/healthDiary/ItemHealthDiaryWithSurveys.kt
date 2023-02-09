package com.petapp.capybara.core.database.entity.healthDiary

import androidx.room.Embedded
import androidx.room.Relation

data class ItemHealthDiaryWithSurveys(
    @Embedded val item: ItemHealthDiaryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "type_id"
    )
    val surveys: List<SurveyHealthDiaryEntity>
)
