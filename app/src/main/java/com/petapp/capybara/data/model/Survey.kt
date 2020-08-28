package com.petapp.capybara.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Survey(
    val id: String,
    val typeId: String,
    val profileId: String,
    val name: String,
    val date: String
): Parcelable