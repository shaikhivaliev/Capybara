package com.petapp.capybara.core.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    val id: Long,
    val name: String,
    val color: Int,
    val photo: String,
    val surveys: List<Survey> = emptyList()
) : Parcelable
