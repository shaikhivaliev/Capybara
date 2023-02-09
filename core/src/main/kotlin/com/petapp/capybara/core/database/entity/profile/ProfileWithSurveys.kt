package com.petapp.capybara.core.database.entity.profile

import androidx.room.Embedded
import androidx.room.Relation
import com.petapp.capybara.core.database.entity.survey.SurveyEntity

data class ProfileWithSurveys(
    @Embedded val profile: ProfileEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "profile_id"
    )
    val surveys: List<SurveyEntity>
)
