package com.petapp.capybara.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.data.model.healthDiary.HealthDiary
import com.petapp.capybara.data.model.healthDiary.HealthDiaryForProfile
import com.petapp.capybara.data.model.healthDiary.HealthDiaryType
import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary
import com.petapp.capybara.ui.data.Chip
import com.petapp.capybara.ui.data.IconTitleDescription

fun List<ItemHealthDiary>.toPresentationModel(): List<HealthDiary> = this.flatMap {
    listOf(it) + if (it.isExpanded && it.surveys.isNotEmpty()) {
        it.surveys.sortedBy { survey -> survey.date }.reversed()
    } else if (it.isExpanded) {
        listOf(it.emptyItem)
    } else emptyList()
}

fun List<ItemHealthDiary>.toPresentationModel(profileId: Long): HealthDiaryForProfile {
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

@Composable
fun Type.toUiData(): IconTitleDescription {
    return IconTitleDescription(
        icon = icon,
        title = name,
        description = stringResource(R.string.surveys_amount, surveys.size.toString())
    )
}

@Composable
fun Profile.toUiData(): IconTitleDescription {
    return IconTitleDescription(
        icon = photo,
        title = name,
        description = stringResource(R.string.surveys_amount, surveys.size.toString())
    )
}

@Composable
fun Survey.toUiData(): IconTitleDescription {
    return IconTitleDescription(
        icon = typeIcon,
        title = name,
        description = date
    )
}

@Composable
fun List<Profile>.toUiData(click: (Long) -> Unit): List<Chip> {
    return this.map {
        Chip(
            id = it.id,
            selected = true,
            text = it.name,
            color = it.color,
            click = click
        )
    }
}
