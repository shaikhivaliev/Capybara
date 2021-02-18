package com.petapp.capybara.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Mark(
    val id: Long,
    val name: String,
    val color: Int
) : Parcelable
