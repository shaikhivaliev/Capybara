package com.petapp.capybara.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.petapp.capybara.database.entity.profile.ProfileEntity
import com.petapp.capybara.database.entity.survey.SurveyEntity
import com.petapp.capybara.database.entity.type.TypeEntity
import com.petapp.capybara.database.entity.healthDiary.ItemHealthDiaryEntity
import com.petapp.capybara.database.entity.healthDiary.SurveyHealthDiaryEntity

@Database(
    entities = [
        ProfileEntity::class,
        TypeEntity::class,
        SurveyEntity::class,
        ItemHealthDiaryEntity::class,
        SurveyHealthDiaryEntity::class
    ],
    version = 1
)
@TypeConverters(HealthDiaryTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao
}
