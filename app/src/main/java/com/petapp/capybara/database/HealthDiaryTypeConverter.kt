package com.petapp.capybara.database

import androidx.room.TypeConverter
import com.petapp.capybara.data.model.healthDiary.HealthDiaryType

open class HealthDiaryTypeConverter {

    @TypeConverter
    fun fromType(value: HealthDiaryType): String = value.name

    @TypeConverter
    fun toType(value: String): HealthDiaryType = enumValueOf(value)
}
