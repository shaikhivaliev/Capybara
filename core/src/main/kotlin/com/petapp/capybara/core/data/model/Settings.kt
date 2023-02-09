package com.petapp.capybara.core.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Settings(
    val id: Long,
    @DrawableRes val image: Int,
    @StringRes val name: Int
)
