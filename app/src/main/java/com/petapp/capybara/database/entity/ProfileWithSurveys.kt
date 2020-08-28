package com.petapp.capybara.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity(tableName = "profile_survey")
data class ProfileWithSurveys(
    @Embedded val profile: ProfileEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "profile_id"
    )
    val surveys: List<SurveyEntity>
)