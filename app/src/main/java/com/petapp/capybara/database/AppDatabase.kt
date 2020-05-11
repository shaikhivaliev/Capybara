package com.petapp.capybara.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.petapp.capybara.profiles.data.ProfileEntity

@Database(entities = [ProfileEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

}