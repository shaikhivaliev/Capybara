package com.petapp.capybara.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "survey")
data class SurveyEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "type_id")
    val typeId: String,
    @ColumnInfo(name = "title")
    val name: String,
    @ColumnInfo(name = "date")
    var date: String
)
