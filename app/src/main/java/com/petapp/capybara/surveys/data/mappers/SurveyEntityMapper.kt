package com.petapp.capybara.surveys.data.mappers

import com.petapp.capybara.database.entity.SurveyEntity
import com.petapp.capybara.surveys.domain.dto.Survey

class SurveyEntityMapper {

    fun transformToSurvey(surveyEntity: SurveyEntity): Survey {
        return Survey(
            id = surveyEntity.id,
            typeId = surveyEntity.typeId,
            name = surveyEntity.name
        )
    }

    fun transformToSurveyEntity(survey: Survey): SurveyEntity {
        return SurveyEntity(
            id = survey.id,
            typeId = survey.typeId,
            name = survey.name
        )
    }

    fun transformToSurvey(surveyEntities: List<SurveyEntity>): List<Survey> {
        val surveys = arrayListOf<Survey>()
        for (surveyEntity in surveyEntities) {
            val survey = transformToSurvey(surveyEntity)
            surveys.add(survey)
        }
        return surveys
    }
}