package com.petapp.capybara.data.model

import android.os.Parcelable
import com.petapp.capybara.core.list.ListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    override val id: Long,
    val name: String,
    val color: Int,
    val photo: String,
    val surveys: List<Survey> = emptyList()
) : Parcelable, ListItem
