package com.petapp.capybara.profiles.domain.dto

data class Profile(
    val id: String,
    val name: String,
    val color: Int,
    val photo: String?
)
