package com.petapp.capybara.core.database

import androidx.room.TypeConverter
import com.petapp.capybara.core.data.model.HealthDiaryType

open class HealthDiaryTypeConverter {

    @TypeConverter
    fun fromType(value: HealthDiaryType): String = value.name

    @TypeConverter
    fun toType(value: String): HealthDiaryType = enumValueOf(value)
}
