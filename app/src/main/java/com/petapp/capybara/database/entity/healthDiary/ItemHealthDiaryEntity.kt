package com.petapp.capybara.database.entity.healthDiary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.petapp.capybara.data.model.HealthDiaryType
import com.petapp.capybara.database.HealthDiaryTypeConverter

@Entity(tableName = "health_diary")
data class ItemHealthDiaryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "type")
    @TypeConverters(HealthDiaryTypeConverter::class)
    var type: HealthDiaryType
)
