package com.petapp.capybara.model

import com.petapp.capybara.ui.common.RecyclerItems

data class EditProfile(
    override val id: Int = UniqueId.id,
    val parentId: Int,
    var color: Color = Color(parentId = parentId),
    var isExpandedColor: Boolean = false
) : RecyclerItems()