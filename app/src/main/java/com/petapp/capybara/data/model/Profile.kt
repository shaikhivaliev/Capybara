package com.petapp.capybara.data.model

import android.os.Parcelable
import com.petapp.capybara.common.ListItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Profile(
    override val id: Long,
    val name: String,
    val color: Int,
    val photo: String? = null,
    val surveys: List<Survey> = emptyList()
) : Parcelable, ListItem
