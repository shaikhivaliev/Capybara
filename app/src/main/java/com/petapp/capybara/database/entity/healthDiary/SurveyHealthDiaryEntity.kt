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
            childColumns = ["type_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SurveyHealthDiaryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "type_id")
    val typeId: Long,
    @ColumnInfo(name = "profile_id")
    val profileId: Long,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "time")
    val time: String,
    @ColumnInfo(name = "survey_value")
    val surveyValue: String,
    @ColumnInfo(name = "unit_of_measure")
    val unitOfMeasure: String
)
