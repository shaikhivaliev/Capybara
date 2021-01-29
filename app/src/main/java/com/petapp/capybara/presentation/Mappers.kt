package com.petapp.capybara.presentation

import com.petapp.capybara.data.model.HealthDiary
import com.petapp.capybara.data.model.ItemHealthDiary

fun List<ItemHealthDiary>.toPresentationModel(): List<HealthDiary> = flatMap {
    listOf(it) + if (it.isExpanded && it.subItems.isNotEmpty()) {
        it.subItems
    } else if (it.isExpanded) {
        listOf(it.emptyItem)
    } else emptyList()
}
