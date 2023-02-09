package com.petapp.capybara.core.database.entity.healthDiary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.petapp.capybara.core.database.HealthDiaryTypeConverter
import com.petapp.capybara.core.data.model.HealthDiaryType

@Entity(tableName = "health_diary")
data class ItemHealthDiaryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "type")
    @TypeConverters(HealthDiaryTypeConverter::class)
    var type: HealthDiaryType
)
