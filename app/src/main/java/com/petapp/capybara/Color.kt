package com.petapp.capybara

import com.petapp.capybara.common.RecyclerItems

data class Color(
    override val id: Int = UniqueId.id,
    val parentId: Int,
    var chosenColor: Int = android.R.color.white
) : RecyclerItems()