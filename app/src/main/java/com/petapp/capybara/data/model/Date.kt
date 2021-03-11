package com.petapp.capybara.data.model

import com.petapp.capybara.common.ListItem
import java.util.Date

data class Date(
    override val id: Long,
    val calendar: Date
) : ListItem
