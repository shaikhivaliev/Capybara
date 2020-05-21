package com.petapp.capybara.profiles.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val name: String,
    @ColumnInfo(name = "color")
    var color: Int,
    @ColumnInfo(name = "photo")
    var photo: String?

)
