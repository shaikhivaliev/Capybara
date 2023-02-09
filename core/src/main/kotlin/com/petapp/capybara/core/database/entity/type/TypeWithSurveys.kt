package com.petapp.capybara.core.database.entity.type

import androidx.room.Embedded
import androidx.room.Relation
import com.petapp.capybara.core.database.entity.survey.SurveyEntity

data class TypeWithSurveys(
    @Embedded val type: TypeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "type_id"
    )
    val surveys: List<SurveyEntity>
)
