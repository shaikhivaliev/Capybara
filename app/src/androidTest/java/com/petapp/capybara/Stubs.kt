package com.petapp.capybara

import com.petapp.capybara.database.entity.ProfileEntity
import com.petapp.capybara.database.entity.SurveyEntity
import com.petapp.capybara.database.entity.TypeEntity

object Stubs {
    private const val NAME = "some_name"
    private const val COLOR = 202
    private const val PHOTO = "some_photo_url"
    private const val PROFILE_ID = 101L
    private const val SURVEY_ID = 202L
    private const val TYPE_ID = 303L
    private const val DATE = "some_date"
    private const val ICON = 303
    private const val MONTH_YEAR = "some_month"
    private const val PROFILE_ICON = "some_icon"

    val PROFILE_ENTITY = ProfileEntity(
        id = PROFILE_ID,
        name = NAME,
        color = COLOR,
        photo = PHOTO
    )
    val SURVEY_ENTITY = SurveyEntity(
        id = SURVEY_ID,
        typeId = TYPE_ID,
        profileId = PROFILE_ID,
        color = COLOR,
        name = NAME,
        date = DATE,
        monthYear = MONTH_YEAR,
        profileIcon = PROFILE_ICON
    )

    val TYPE_ENTITY = TypeEntity(
        id = TYPE_ID,
        name = NAME,
        icon = ICON
    )
}
