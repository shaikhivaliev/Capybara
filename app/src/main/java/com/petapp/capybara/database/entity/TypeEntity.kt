package com.petapp.capybara.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "type")
data class TypeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val name: String,
    @ColumnInfo(name = "amount")
    val amount: String?,
    @ColumnInfo(name = "icon")
    val icon: Int
)
