package com.petapp.capybara.data.model

import android.os.Parcelable
import com.petapp.capybara.common.ListItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Survey(
    override val id: Long,
    val typeId: Long,
    val profileId: Long,
    val color: Int,
    val name: String,
    val date: String,
    val monthYear: String
) : Parcelable, ListItem
