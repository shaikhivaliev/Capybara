package com.petapp.capybara.data.model

import com.petapp.capybara.core.list.ListItem
import java.util.Date

data class DateModel(
    override val id: Long,
    val calendar: Date
) : ListItem
