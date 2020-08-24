package com.petapp.capybara.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.petapp.capybara.R
import com.petapp.capybara.UniqueId
import com.petapp.capybara.database.entity.TypeEntity
import java.util.concurrent.Executors

class DatabaseProvider(context: Context) {

    private val setInitialData: RoomDatabase.Callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            Executors.newSingleThreadExecutor().execute {
                val typeBlood = TypeEntity(
                    UniqueId.id.toString(),
                    context.getString(R.string.type_default_blood),
                    R.drawable.ic_blood
                )
                database.appDao().insertType(typeBlood)
                val typeHeart = TypeEntity(
                    UniqueId.id.toString(),
                    context.getString(R.string.type_default_heart),
                    R.drawable.ic_heart
                )
                database.appDao().insertType(typeHeart)
                val typeTeeth = TypeEntity(
                    UniqueId.id.toString(),
                    context.getString(R.string.type_default_teeth),
                    R.drawable.ic_teeth
                )
                database.appDao().insertType(typeTeeth)
            }
        }
    }

    private val database: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        context.getString(R.string.database_name)
    )
        .addCallback(setInitialData)
        .fallbackToDestructiveMigration()
        .build()

    fun appDao() = database.appDao()
}