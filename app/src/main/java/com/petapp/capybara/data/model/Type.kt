package com.petapp.capybara.data.model

data class Type(
    val id: String?,
    val name: String,
    val icon: Int,
    val surveys: List<Survey> = emptyList()
)
