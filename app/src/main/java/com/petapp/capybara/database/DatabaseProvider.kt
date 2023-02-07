package com.petapp.capybara.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.petapp.capybara.R
import com.petapp.capybara.data.model.healthDiary.HealthDiaryType
import com.petapp.capybara.database.entity.type.TypeEntity
import com.petapp.capybara.database.entity.healthDiary.ItemHealthDiaryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class DatabaseProvider(private val context: Context) {

    private val setInitialData: RoomDatabase.Callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(IO).launch {
                createTypes()
                createHealthDiaryItems()
            }
        }
    }

    private suspend fun createHealthDiaryItems() {
        val itemBloodPressure = ItemHealthDiaryEntity(
            HealthDiaryType.BLOOD_PRESSURE.ordinal.toLong(),
            HealthDiaryType.BLOOD_PRESSURE
        )
        database.appDao().createHealthDiaryItem(itemBloodPressure)

        val itemPulse = ItemHealthDiaryEntity(
            HealthDiaryType.PULSE.ordinal.toLong(),
            HealthDiaryType.PULSE
        )
        database.appDao().createHealthDiaryItem(itemPulse)

        val itemBloodGlucose = ItemHealthDiaryEntity(
            HealthDiaryType.BLOOD_GLUCOSE.ordinal.toLong(),
            HealthDiaryType.BLOOD_GLUCOSE
        )
        database.appDao().createHealthDiaryItem(itemBloodGlucose)

        val itemHeight = ItemHealthDiaryEntity(
            HealthDiaryType.HEIGHT.ordinal.toLong(),
            HealthDiaryType.HEIGHT
        )
        database.appDao().createHealthDiaryItem(itemHeight)

        val itemWeight = ItemHealthDiaryEntity(
            HealthDiaryType.WEIGHT.ordinal.toLong(),
            HealthDiaryType.WEIGHT
        )
        database.appDao().createHealthDiaryItem(itemWeight)
    }

    private suspend fun createTypes() {
        val typeVaccinations = TypeEntity(
            0L,
            context.getString(R.string.type_default_vaccinations),
            R.drawable.ic_vaccination
        )
        database.appDao().createType(typeVaccinations)

        val typeAnalyzes = TypeEntity(
            0L,
            context.getString(R.string.type_default_analyzes),
            R.drawable.ic_analyzes
        )
        database.appDao().createType(typeAnalyzes)

        val typeRecipes = TypeEntity(
            0L,
            context.getString(R.string.type_default_recipes),
            R.drawable.ic_recipes
        )
        database.appDao().createType(typeRecipes)

        val typeSurveys = TypeEntity(
            0L,
            context.getString(R.string.type_default_surveys),
            R.drawable.ic_type_surveys
        )
        database.appDao().createType(typeSurveys)
    }

    private val database: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database.db"
    )
        .addCallback(setInitialData)
        .fallbackToDestructiveMigrationOnDowngrade()
        .build()

    fun appDao() = database.appDao()
}
