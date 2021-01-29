package com.petapp.capybara.database.entity.healthDiary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "survey_health_diary",
    foreignKeys = [
        ForeignKey(
            entity = ItemHealthDiaryEntity::class,
            parentColumns = ["id"],
            childColumns = ["item_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SurveyHealthDiaryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "item_id")
    val itemId: Int
)
