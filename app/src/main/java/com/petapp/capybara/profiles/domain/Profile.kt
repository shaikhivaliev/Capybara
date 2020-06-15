package com.petapp.capybara.profiles.domain

import com.petapp.capybara.common.BaseItem
import com.petapp.capybara.common.UniqueId

class Profile(
    override var id: Int = UniqueId.generateId(),
    var title: String = "",
    var color: Int = android.R.color.white,
    var isShowEditItem: Boolean = false,
    var profileEdit: ProfileEdit = ProfileEdit(
        parentId = id,
        parentColor = color,
        parentTitle = title,
        isShowColorsItem = false,
        profileColor = ProfileColor(
            parentId = id,
            chosenColor = color
        )
    )
) : BaseItem()