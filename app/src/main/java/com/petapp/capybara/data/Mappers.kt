package com.petapp.capybara.data

import com.petapp.capybara.data.model.*
import com.petapp.capybara.database.entity.*
import com.petapp.capybara.database.entity.healthDiary.ItemHealthDiaryWithSurveys
import com.petapp.capybara.database.entity.healthDiary.SurveyHealthDiaryEntity

fun ProfileEntity.toMark(): Mark {
    return Mark(
        id = id.toString(),
        name = name,
        color = color
    )
}

fun List<ProfileEntity>.toMarks(): List<Mark> {
    val marks = arrayListOf<Mark>()
    for (profileEntity in this) {
        val mark = profileEntity.toMark()
        marks.add(mark)
    }
    return marks
}

fun ProfileEntity.toProfile(): Profile {
    return Profile(
        id = id.toString(),
        name = name,
        color = color,
        photo = photo
    )
}

fun ProfileWithSurveys.toProfile(): Profile {
    return Profile(
        id = profile.id.toString(),
        name = profile.name,
        color = profile.color,
        photo = profile.photo,
        surveys = surveys.toSurveys()
    )
}

fun Profile.toProfileEntity(): ProfileEntity {
    return ProfileEntity(
        id = id.toLong(),
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
        id = id.toString(),
        typeId = typeId,
        profileId = profileId,
        color = color,
        name = name,
        date = date,
        month = month
    )
}

fun Survey.toSurveyEntity(): SurveyEntity {
    return SurveyEntity(
        id = this.id.toLong(),
        typeId = this.typeId,
        profileId = this.profileId,
        color = this.color,
        name = this.name,
        date = this.date,
        month = this.month
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
        id = id.toString(),
        name = name,
        icon = icon
    )
}

fun TypeWithSurveys.toType(): Type {
    return Type(
        id = type.id.toString(),
        name = type.name,
        icon = type.icon,
        surveys = surveys.toSurveys()
    )
}

fun Type.toTypeEntity(): TypeEntity {
    return TypeEntity(
        id = id.toLong(),
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
        id = id.toLong(),
        typeId = type.ordinal.toString(),
        profileId = profileId,
        date = date,
        time = time,
        surveyValue = surveyValue,
        unitOfMeasure = unitOfMeasure
    )
}

fun SurveyHealthDiaryEntity.toSurveyHealthDiary(): SurveyHealthDiary {
    return SurveyHealthDiary(
        id = id.toString(),
        type = typeId.toHealthDiary(),
        profileId = profileId,
        date = date,
        time = time,
        surveyValue = surveyValue,
        unitOfMeasure = unitOfMeasure
    )
}

private fun String.toHealthDiary(): HealthDiaryType = when (this) {
    HealthDiaryType.BLOOD_PRESSURE.ordinal.toString() -> HealthDiaryType.BLOOD_PRESSURE
    HealthDiaryType.PULSE.ordinal.toString() -> HealthDiaryType.PULSE
    HealthDiaryType.BLOOD_GLUCOSE.ordinal.toString() -> HealthDiaryType.BLOOD_GLUCOSE
    HealthDiaryType.HEIGHT.ordinal.toString() -> HealthDiaryType.HEIGHT
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
