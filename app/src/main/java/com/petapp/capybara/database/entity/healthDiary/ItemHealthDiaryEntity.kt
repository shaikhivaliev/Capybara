package com.petapp.capybara.database.entity.healthDiary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.petapp.capybara.data.model.healthDiary.HealthDiaryType
import com.petapp.capybara.database.HealthDiaryTypeConverter

@Entity(tableName = "health_diary")
data class ItemHealthDiaryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "type")
    @TypeConverters(HealthDiaryTypeConverter::class)
    var type: HealthDiaryType
)
