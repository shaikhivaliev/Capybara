package com.petapp.capybara.core.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Type(
    val id: Long,
    val name: String,
    val icon: Int,
    val surveys: List<Survey> = emptyList()
) : Parcelable
