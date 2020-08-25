package com.petapp.capybara.data.model

data class Type(
    val id: Long?,
    val name: String,
    val icon: Int,
    val surveys: List<Survey> = emptyList()
)
