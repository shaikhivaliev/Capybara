package com.petapp.capybara.model

import com.petapp.capybara.ui.common.RecyclerItems

data class Color(
    override val id: Int = UniqueId.id,
    val parentId: Int,
    var chosenColor: Int = android.R.color.white
) : RecyclerItems()