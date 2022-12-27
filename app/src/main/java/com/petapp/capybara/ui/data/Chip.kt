package com.petapp.capybara.ui.data

data class Chip(
    val id: Long,
    val selected: Boolean,
    val text: String,
    val color: Int,
    val click: (Long) -> Unit
)
