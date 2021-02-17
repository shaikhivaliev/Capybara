package com.petapp.capybara.presentation

import com.petapp.capybara.data.model.healthDiary.HealthDiary
import com.petapp.capybara.data.model.healthDiary.HealthDiaryForProfile
import com.petapp.capybara.data.model.healthDiary.HealthDiaryType
import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary

fun List<ItemHealthDiary>.toPresentationModel(): List<HealthDiary> = flatMap {
    listOf(it) + if (it.isExpanded && it.surveys.isNotEmpty()) {
        it.surveys.sortedBy { survey -> survey.date }.reversed()
    } else if (it.isExpanded) {
        listOf(it.emptyItem)
    } else emptyList()
}

fun List<ItemHealthDiary>.toPresentationModel(profileId: String): HealthDiaryForProfile {
    val profilesItems = this.map { item ->
        val surveys = item.surveys.filter { it.profileId == profileId }
        item.surveys = surveys.sortedBy { survey -> survey.date }.reversed()
        item
    }
    val healthDiaryForProfile = HealthDiaryForProfile.Builder()
    profilesItems.forEach {
        when (it.type) {
            HealthDiaryType.BLOOD_PRESSURE ->
                if (it.surveys.isNotEmpty()) healthDiaryForProfile.bloodPressure(it.surveys.first().surveyValue)
            HealthDiaryType.PULSE ->
                if (it.surveys.isNotEmpty()) healthDiaryForProfile.pulse(it.surveys.first().surveyValue)

            HealthDiaryType.BLOOD_GLUCOSE ->
                if (it.surveys.isNotEmpty()) healthDiaryForProfile.bloodGlucose(it.surveys.first().surveyValue)

            HealthDiaryType.HEIGHT ->
                if (it.surveys.isNotEmpty()) healthDiaryForProfile.height(it.surveys.first().surveyValue)

            HealthDiaryType.WEIGHT ->
                if (it.surveys.isNotEmpty()) healthDiaryForProfile.weight(it.surveys.first().surveyValue)
        }
    }
    return healthDiaryForProfile.build()
}
