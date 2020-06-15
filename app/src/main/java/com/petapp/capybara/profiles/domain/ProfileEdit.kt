package com.petapp.capybara.profiles.domain

import com.petapp.capybara.common.BaseItem
import com.petapp.capybara.common.UniqueId

data class ProfileEdit(
    override val id: Int = UniqueId.generateId(),
    val parentId: Int,
    val parentColor: Int,
    var parentTitle: String,
    var isShowColorsItem: Boolean,
    var profileColor: ProfileColor
) : BaseItem()