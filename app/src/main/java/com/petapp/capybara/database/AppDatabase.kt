package com.petapp.capybara.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.petapp.capybara.database.entity.ProfileEntity
import com.petapp.capybara.database.entity.SurveyEntity
import com.petapp.capybara.database.entity.TypeEntity

// home task #7 - persistent storage
@Database(entities = [ProfileEntity::class, TypeEntity::class, SurveyEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao
}