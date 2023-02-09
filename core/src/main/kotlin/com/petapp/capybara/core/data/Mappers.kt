package com.petapp.capybara.core.data

import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.model.Survey
import com.petapp.capybara.core.data.model.Type
import com.petapp.capybara.core.database.entity.healthDiary.ItemHealthDiaryWithSurveys
import com.petapp.capybara.core.database.entity.healthDiary.SurveyHealthDiaryEntity
import com.petapp.capybara.core.database.entity.profile.ProfileEntity
import com.petapp.capybara.core.database.entity.profile.ProfileWithSurveys
import com.petapp.capybara.core.database.entity.survey.SurveyEntity
import com.petapp.capybara.core.database.entity.type.TypeEntity
import com.petapp.capybara.core.database.entity.type.TypeWithSurveys
import com.petapp.capybara.core.data.model.HealthDiaryType
import com.petapp.capybara.core.data.model.ItemHealthDiary
import com.petapp.capybara.core.data.model.SurveyHealthDiary

fun ProfileEntity.toProfile(): Profile {
    return Profile(
        id = id,
        name = name,
        color = color,
        photo = photo
    )
}

fun ProfileWithSurveys.toProfile(): Profile {
    return Profile(
        id = profile.id,
        name = profile.name,
        color = profile.color,
        photo = profile.photo,
        surveys = surveys.toSurveys()
    )
}

fun Profile.toProfileEntity(): ProfileEntity {
    return ProfileEntity(
        id = id,
        name = name,
        color = color,
        photo = photo
    )
}

fun SurveyEntity.toSurvey(): Survey {
    return Survey(
        id = id,
        typeId = typeId,
        profileId = profileId,
        color = color,
        name = name,
        date = date,
        monthYear = monthYear,
        profileIcon = profileIcon,
        typeIcon = typeIcon
    )
}

fun Survey.toSurveyEntity(): SurveyEntity {
    return SurveyEntity(
        id = id,
        typeId = typeId,
        profileId = profileId,
        color = color,
        name = name,
        date = date,
        monthYear = monthYear,
        profileIcon = profileIcon,
        typeIcon = typeIcon
    )
}

fun List<SurveyEntity>.toSurveys(): List<Survey> {
    val surveys = arrayListOf<Survey>()
    for (surveyEntity in this) {
        val survey = surveyEntity.toSurvey()
        surveys.add(survey)
    }
    return surveys
}

fun TypeEntity.toType(): Type {
    return Type(
        id = id,
        name = name,
        icon = icon
    )
}

fun TypeWithSurveys.toType(): Type {
    return Type(
        id = type.id,
        name = type.name,
        icon = type.icon,
        surveys = surveys.toSurveys()
    )
}

fun Type.toTypeEntity(): TypeEntity {
    return TypeEntity(
        id = id,
        name = name,
        icon = icon
    )
}

fun ItemHealthDiaryWithSurveys.toHealthDiaryItem(): ItemHealthDiary {
    return ItemHealthDiary(
        id = item.id,
        type = item.type,
        surveys = surveys.toHealthDiarySurveys()
    )
}

fun SurveyHealthDiary.toSurveyHealthDiaryEntity(): SurveyHealthDiaryEntity {
    return SurveyHealthDiaryEntity(
        id = id,
        typeId = type.ordinal.toLong(),
        profileId = profileId,
        date = date,
        time = time,
        surveyValue = surveyValue,
        unitOfMeasure = unitOfMeasure
    )
}

fun SurveyHealthDiaryEntity.toSurveyHealthDiary(): SurveyHealthDiary {
    return SurveyHealthDiary(
        id = id,
        type = typeId.toHealthDiary(),
        profileId = profileId,
        date = date,
        time = time,
        surveyValue = surveyValue,
        unitOfMeasure = unitOfMeasure
    )
}

private fun Long.toHealthDiary(): HealthDiaryType = when (this) {
    HealthDiaryType.BLOOD_PRESSURE.ordinal.toLong() -> HealthDiaryType.BLOOD_PRESSURE
    HealthDiaryType.PULSE.ordinal.toLong() -> HealthDiaryType.PULSE
    HealthDiaryType.BLOOD_GLUCOSE.ordinal.toLong() -> HealthDiaryType.BLOOD_GLUCOSE
    HealthDiaryType.HEIGHT.ordinal.toLong() -> HealthDiaryType.HEIGHT
    else -> HealthDiaryType.WEIGHT
}

fun List<SurveyHealthDiaryEntity>.toHealthDiarySurveys(): List<SurveyHealthDiary> {
    val surveys = arrayListOf<SurveyHealthDiary>()
    for (surveyEntity in this) {
        val survey = surveyEntity.toSurveyHealthDiary()
        surveys.add(survey)
    }
    return surveys
}
