package com.petapp.capybara.data

import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.data.model.healthDiary.HealthDiaryType
import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary
import com.petapp.capybara.data.model.healthDiary.SurveyHealthDiary
import com.petapp.capybara.database.entity.*
import com.petapp.capybara.database.entity.healthDiary.ItemHealthDiaryWithSurveys
import com.petapp.capybara.database.entity.healthDiary.SurveyHealthDiaryEntity

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

fun List<ProfileWithSurveys>.toProfiles(): List<Profile> {
    val profiles = arrayListOf<Profile>()
    for (profileEntity in this) {
        val profile = profileEntity.toProfile()
        profiles.add(profile)
    }
    return profiles
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

fun List<TypeWithSurveys>.toTypes(): List<Type> {
    val types = arrayListOf<Type>()
    for (typeEntity in this) {
        val profile = typeEntity.toType()
        types.add(profile)
    }
    return types
}

fun List<ItemHealthDiaryWithSurveys>.toHealthDiaryItems(): List<ItemHealthDiary> {
    val items = arrayListOf<ItemHealthDiary>()
    for (itemEntity in this) {
        val survey = itemEntity.toHealthDiaryItem()
        items.add(survey)
    }
    return items
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
