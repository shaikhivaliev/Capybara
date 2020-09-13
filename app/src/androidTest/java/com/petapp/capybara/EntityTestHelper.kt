package com.petapp.capybara

import com.petapp.capybara.database.entity.ProfileEntity
import com.petapp.capybara.database.entity.SurveyEntity
import com.petapp.capybara.database.entity.TypeEntity

class EntityTestHelper {
    companion object {
        val TYPE = TypeEntity(
            name = "MockType",
            icon = R.drawable.ic_blood,
        )
        val PROFILE = ProfileEntity(
            name = "MockProfile",
            color = android.R.color.black,
            photo = "Some mock url"
        )
        val SURVEY = SurveyEntity(
            typeId = "",
            profileId = "",
            color = android.R.color.black,
            name = "MockSurvey",
            date = "12.12.2012"
        )
    }
}
