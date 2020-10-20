package com.petapp.capybara.data

import com.petapp.capybara.data.model.Mark
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.database.entity.*

fun ProfileEntity.toMark(): Mark {
    return Mark(
        id = this.id.toString(),
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
        id = this.id.toString(),
        name = this.name,
        color = this.color,
        photo = this.photo
    )
}

fun ProfileWithSurveys.toProfile(): Profile {
    return Profile(
        id = this.profile.id.toString(),
        name = this.profile.name,
        color = this.profile.color,
        photo = this.profile.photo,
        surveys = this.surveys.toSurveys()
    )
}

fun Profile.toProfileEntity(): ProfileEntity {
    return ProfileEntity(
        id = this.id.toLong(),
        name = this.name,
        color = this.color,
        photo = this.photo
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
        id = this.id.toString(),
        typeId = this.typeId,
        profileId = this.profileId,
        color = this.color,
        name = this.name,
        date = this.date,
        month = this.month
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
        id = this.id.toString(),
        name = this.name,
        icon = this.icon
    )
}

fun TypeWithSurveys.toType(): Type {
    return Type(
        id = this.type.id.toString(),
        name = this.type.name,
        icon = this.type.icon,
        surveys = this.surveys.toSurveys()
    )
}

fun Type.toTypeEntity(): TypeEntity {
    return TypeEntity(
        id = this.id.toLong(),
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
