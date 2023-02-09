package com.petapp.capybara.core.data.model

import android.os.Parcelable
import com.petapp.capybara.core.BaseNavDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class Survey(
    val id: Long,
    val typeId: Long,
    val profileId: Long,
    val color: Int,
    val name: String,
    val date: String,
    val monthYear: String,
    val profileIcon: String,
    val typeIcon: Int
) : Parcelable, BaseNavDto
