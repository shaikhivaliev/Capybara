package com.petapp.capybara.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "type")
data class TypeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long?,
    @ColumnInfo(name = "title")
    val name: String,
    @ColumnInfo(name = "icon")
    val icon: Int
)
