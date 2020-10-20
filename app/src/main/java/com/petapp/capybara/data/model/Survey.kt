package com.petapp.capybara.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Survey(
    val id: String,
    val typeId: String,
    val profileId: String,
    val color: Int,
    val name: String,
    val date: String,
    val month: String
) : Parcelable
