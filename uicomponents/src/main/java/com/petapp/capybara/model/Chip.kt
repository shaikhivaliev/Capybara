package com.petapp.capybara.model

data class Chip(
    val id: Long,
    val selected: Boolean,
    val text: String,
    val color: Int,
    val click: (Long) -> Unit
)
