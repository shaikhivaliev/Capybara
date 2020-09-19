package com.petapp.capybara.utils

import com.petapp.capybara.R
import com.petapp.capybara.data.model.Mark
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.database.entity.ProfileEntity
import com.petapp.capybara.database.entity.SurveyEntity
import com.petapp.capybara.database.entity.TypeEntity
import com.petapp.capybara.database.entity.TypeWithSurveys

object Stubs {
    private const val NAME = "some_name"
    private const val ID_LONG = 101L
    private const val ID_STRING = "some_string"
    private const val COLOR = 202
    private const val PHOTO = "some_photo_url"
    private const val TYPE_ID = "some_type_id"
    private const val PROFILE_ID = "some_profile_id"
    private const val DATE = "some_date"
    private const val ICON = 303
    const val PROFILES_ERROR = R.string.error_get_profiles
    const val SURVEYS_ERROR = R.string.error_get_surveys
    const val TYPES_ERROR = R.string.error_get_types

    val PROFILE_ENTITY = ProfileEntity(
        id = ID_LONG,
        name = NAME,
        color = COLOR,
        photo = PHOTO
    )
    val PROFILE = Profile(
        id = ID_STRING,
        name = NAME,
        color = COLOR,
        photo = PHOTO
    )
    val SURVEY_ENTITY = SurveyEntity(
        id = ID_LONG,
        typeId = TYPE_ID,
        profileId = PROFILE_ID,
        color = COLOR,
        name = NAME,
        date = DATE
    )

    val SURVEY = Survey(
        id = ID_STRING,
        typeId = TYPE_ID,
        profileId = PROFILE_ID,
        color = COLOR,
        name = NAME,
        date = DATE
    )

    val TYPE_ENTITY = TypeEntity(
        id = ID_LONG,
        name = NAME,
        icon = ICON
    )

    val TYPE = Type(
        id = ID_STRING,
        name = NAME,
        icon = ICON
    )

    val MARK = Mark(
        id = ID_STRING,
        name = NAME,
        color = COLOR
    )

    val TYPE_SURVEYS = TypeWithSurveys(
        type = TYPE_ENTITY,
        surveys = listOf(SURVEY_ENTITY, SURVEY_ENTITY, SURVEY_ENTITY)
    )
}
