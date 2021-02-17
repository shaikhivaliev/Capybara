package com.petapp.capybara.data.model

import android.os.Parcelable
import com.petapp.capybara.common.ListItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Type(
    val id: String,
    val name: String,
    val icon: Int,
    val surveys: List<Survey> = emptyList()
) : Parcelable, ListItem
