package com.petapp.capybara

import com.petapp.capybara.common.RecyclerItems

data class EditProfile(
    override val id: Int = UniqueId.id,
    val parentId: Int,
    var color: Color = Color(parentId = parentId),
    var isExpandedColor: Boolean = false
) : RecyclerItems()