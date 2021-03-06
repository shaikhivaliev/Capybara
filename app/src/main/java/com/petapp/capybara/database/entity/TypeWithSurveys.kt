package com.petapp.capybara.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TypeWithSurveys(
    @Embedded val type: TypeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "type_id"
    )
    val surveys: List<SurveyEntity>
)
