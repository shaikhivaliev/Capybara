package com.petapp.capybara.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.petapp.capybara.common.ListItem

data class Settings(
    override val id: Long,
    @DrawableRes val image: Int,
    @StringRes val name: Int
) : ListItem
