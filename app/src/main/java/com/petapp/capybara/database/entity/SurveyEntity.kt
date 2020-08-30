package com.petapp.capybara.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// home task #7 - persistent storage
@Entity(
    tableName = "survey",
    foreignKeys = [
    ForeignKey(
        entity = TypeEntity::class,
        parentColumns = ["id"],
        childColumns = ["type_id"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profile_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SurveyEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "type_id")
    val typeId: String,
    @ColumnInfo(name = "profile_id")
    val profileId: String,
    @ColumnInfo(name = "color")
    val color: Int,
    @ColumnInfo(name = "title")
    val name: String,
    @ColumnInfo(name = "date")
    var date: String
)
