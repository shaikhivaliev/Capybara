package com.petapp.capybara.data

import com.petapp.capybara.data.model.Mark
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.database.entity.ProfileEntity
import com.petapp.capybara.database.entity.SurveyEntity
import com.petapp.capybara.database.entity.TypeEntity
import com.petapp.capybara.database.entity.TypeWithSurveys

fun ProfileEntity.toMark(): Mark {
    return Mark(
        id = this.id,
        name = this.name,
        color = this.color
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
        id = this.id,
        name = this.name,
        color = this.color,
        photo = this.photo
    )
}

fun Profile.toProfileEntity(): ProfileEntity {
    return ProfileEntity(
        id = this.id,
        name = this.name,
        color = this.color,
        photo = this.photo
    )
}

fun List<ProfileEntity>.toProfiles(): List<Profile> {
    val profiles = arrayListOf<Profile>()
    for (profileEntity in this) {
        val profile = profileEntity.toProfile()
        profiles.add(profile)
    }
    return profiles
}

fun SurveyEntity.toSurvey(): Survey {
    return Survey(
        id = this.id,
        typeId = this.typeId,
        name = this.name
    )
}

fun Survey.toSurveyEntity(): SurveyEntity {
    return SurveyEntity(
        id = this.id,
        typeId = this.typeId,
        name = this.name
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
        id = this.id,
        name = this.name,
        icon = this.icon
    )
}

fun TypeWithSurveys.toType(): Type {
    return Type(
        id = this.type.id,
        name = this.type.name,
        icon = this.type.icon,
        surveys = this.surveys.toSurveys()
    )
}

fun Type.toTypeEntity(): TypeEntity {
    return TypeEntity(
        id = this.id,
        name = this.name,
        icon = this.icon
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