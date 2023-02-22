package com.petapp.capybara.healthdiary

import androidx.compose.runtime.Composable
import com.petapp.capybara.core.data.model.ItemHealthDiary
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.model.Chip

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
