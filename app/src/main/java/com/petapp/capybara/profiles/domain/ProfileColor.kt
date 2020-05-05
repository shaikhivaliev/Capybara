package com.petapp.capybara.profiles.domain

import com.petapp.capybara.common.RecyclerItems
import com.petapp.capybara.common.UniqueId

data class ProfileColor(
    override val id: Int = UniqueId.id,
    val parentId: Int,
    var chosenColor: Int
) : RecyclerItems()