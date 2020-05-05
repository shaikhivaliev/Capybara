package com.petapp.capybara.profiles.domain

import com.petapp.capybara.common.RecyclerItems
import com.petapp.capybara.common.UniqueId

data class ProfileEdit(
    override val id: Int = UniqueId.id,
    val parentId: Int,
    val parentColor: Int,
    var parentTitle: String,
    var isShowColorsItem: Boolean,
    var profileColor: ProfileColor
) : RecyclerItems()