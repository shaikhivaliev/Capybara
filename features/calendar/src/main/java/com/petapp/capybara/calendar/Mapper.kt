package com.petapp.capybara.calendar

import androidx.compose.runtime.Composable
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.model.Survey
import com.petapp.capybara.model.Chip
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun List<Survey>.filterSurveys(id: Long): List<Survey> {
    return this.filter { item -> item.profileId == id }
}

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

fun List<Survey>.toDates(id: Long): List<LocalDate> {
    val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)
    return this
        .filterSurveys(id)
        .map {
            LocalDate.parse(it.date, dateFormat)
        }
}
