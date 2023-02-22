package com.petapp.capybara.survey

import androidx.compose.runtime.Composable
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.model.Survey
import com.petapp.capybara.model.Chip
import com.petapp.capybara.model.IconTitleDescription

@Composable
fun List<Profile>.toUiData(
    selectedChipId: Long,
    click: (Long) -> Unit
): List<Chip> {
    return this.map {
        Chip(
            id = it.id,
            selected = selectedChipId == it.id,
            text = it.name,
            color = it.color,
            click = click
        )
    }
}

fun List<Survey>.filterSurveys(id: Long): List<Survey> {
    return this.filter { item -> item.profileId == id }
}

@Composable
fun Survey.toUiData(): IconTitleDescription {
    return IconTitleDescription(
        icon = typeIcon,
        title = name,
        description = date
    )
}
