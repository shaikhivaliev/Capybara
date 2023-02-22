package com.petapp.capybara.core.data.model

import com.petapp.capybara.core.R

enum class HealthDiaryType(
    val title: Int,
    val measure: Int
) {
    BLOOD_PRESSURE(
        R.string.health_diary_survey_blood_pressure,
        R.string.health_diary_blood_pressure_unit
    ),
    PULSE(
        R.string.health_diary_survey_pulse,
        R.string.health_diary_pulse_unit
    ),
    BLOOD_GLUCOSE(
        R.string.health_diary_survey_blood_glucose,
        R.string.health_diary_blood_glucose_unit
    ),
    HEIGHT(
        R.string.health_diary_survey_height,
        R.string.health_diary_height_unit
    ),
    WEIGHT(
        R.string.health_diary_survey_weight,
        R.string.health_diary_weight_unit
    )
}
