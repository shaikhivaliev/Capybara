package com.petapp.core_impl

import androidx.room.Database
import androidx.room.RoomDatabase
import com.petapp.core_api.database.DatabaseContract
import com.petapp.core_api.database.entity.ProfileEntity

@Database(entities = [ProfileEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(), DatabaseContract