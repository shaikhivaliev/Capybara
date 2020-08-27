package com.petapp.capybara.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Profile(
    val id: String?,
    val name: String,
    val color: Int,
    val photo: String?
) : Parcelable
