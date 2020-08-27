package com.petapp.capybara.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Type(
    val id: String?,
    val name: String,
    val icon: Int,
    val surveys: List<Survey> = emptyList()
): Parcelable
