package com.petapp.capybara.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.petapp.capybara.R
import com.petapp.capybara.core.data.model.ItemHealthDiary
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.model.Survey
import com.petapp.capybara.core.data.model.Type
import com.petapp.capybara.ui.model.Chip
import com.petapp.capybara.ui.model.IconTitleDescription

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

fun List<ItemHealthDiary>.filterHealthDiary(id: Long): List<ItemHealthDiary> {
    return this.map {
        val surveys = it.surveys.filter { item -> item.profileId == id }
        it.copy(surveys = surveys)
    }
}

fun List<ItemHealthDiary>.expandItem(item: ItemHealthDiary): List<ItemHealthDiary> {
    val isExpandedType = this.find { it.type == item.type }?.isExpanded ?: false
    return this.map {
        if (it.type == item.type) {
            it.copy(isExpanded = !isExpandedType)
        } else {
            it
        }
    }
}

fun List<ItemHealthDiary>.expandItems(oldList: List<ItemHealthDiary>): List<ItemHealthDiary> {
    return this.map { item ->
        val type = oldList.find { it.type == item.type }?.type
        val isExpandedType = oldList.find { it.type == item.type }?.isExpanded ?: false
        if (item.type == type) {
            item.copy(isExpanded = isExpandedType)
        } else {
            item
        }
    }
}
