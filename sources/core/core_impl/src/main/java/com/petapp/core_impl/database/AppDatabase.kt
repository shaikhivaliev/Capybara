package com.petapp.core_impl.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.petapp.core_api.database.AppDao
import com.petapp.core_api.database.entity.DatabaseContract
import com.petapp.core_api.database.entity.ProfileEntity

@Database(entities = [ProfileEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(), DatabaseContract