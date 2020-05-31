package com.petapp.capybara.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "survey",
    foreignKeys = [ForeignKey(entity = TypeEntity::class, parentColumns = ["id"], childColumns = ["type_id"], onDelete = CASCADE)]
)
data class SurveyEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "type_id")
    val typeId: String,
    @ColumnInfo(name = "title")
    val name: String
)
