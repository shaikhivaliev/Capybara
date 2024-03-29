package com.petapp.capybara.database.entity.profile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "title")
    val name: String,
    @ColumnInfo(name = "color")
    var color: Int,
    @ColumnInfo(name = "photo")
    var photo: String
)
