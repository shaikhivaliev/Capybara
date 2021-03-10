package com.petapp.capybara.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.petapp.capybara.common.ListItem

data class ImagePicker(
    override val id: Long,
    val type: ImagePickerType,
    @StringRes
    val name: Int,
    @DrawableRes
    val icon: Int
) : ListItem
