package com.petapp.capybara.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "survey")
data class SurveyEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long?,
    @ColumnInfo(name = "type_id")
    val typeId: Long?,
    @ColumnInfo(name = "title")
    val name: String,
    @ColumnInfo(name = "date")
    var date: String
)
