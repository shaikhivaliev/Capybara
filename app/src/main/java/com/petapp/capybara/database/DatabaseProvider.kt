package com.petapp.capybara.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.petapp.capybara.R
import com.petapp.capybara.common.UniqueId
import com.petapp.capybara.database.entity.TypeEntity
import java.util.concurrent.Executors

class DatabaseProvider(context: Context) {

    private val setInitialData: RoomDatabase.Callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            Executors.newSingleThreadExecutor().execute {
                val typeBlood = TypeEntity(UniqueId.id.toString(), "Анализы крови", "0", R.drawable.ic_blood)
                database.appDao().insertType(typeBlood)
                val typeHeart = TypeEntity(UniqueId.id.toString(), "Сердце", "0", R.drawable.ic_heart)
                database.appDao().insertType(typeHeart)
                val typeTeeth = TypeEntity(UniqueId.id.toString(), "Стоматолог", "0", R.drawable.ic_teeth)
                database.appDao().insertType(typeTeeth)
            }
        }
    }
    private val database: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "database")
        .addCallback(setInitialData)
        .fallbackToDestructiveMigration()
        .build()

    fun appDao() = database.appDao()
}