package com.petapp.capybara.profiles.domain

import com.petapp.capybara.common.BaseItem
import com.petapp.capybara.common.UniqueId

data class ProfileColor(
    override val id: Int = UniqueId.generateId(),
    val parentId: Int,
    var chosenColor: Int
) : BaseItem()