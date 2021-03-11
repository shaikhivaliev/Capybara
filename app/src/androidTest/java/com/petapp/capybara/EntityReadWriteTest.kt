package com.petapp.capybara

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.petapp.capybara.database.AppDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EntityReadWriteTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase

    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetTypeById() {
        val type = Stubs.TYPE_ENTITY
        database.appDao().createType(type)

        database.appDao()
            .getType(type.id)
            .test()
            .assertValue {
                it.id == type.id && it.name == type.name
            }
    }

    @Test
    fun insertAndGetSurveyById() {
        val type = Stubs.TYPE_ENTITY
        database.appDao().createType(type)
        val profile = Stubs.PROFILE_ENTITY
        database.appDao().createProfile(profile)
        val survey = Stubs.SURVEY_ENTITY
        database.appDao().createSurvey(survey)

        database.appDao()
            .getSurvey(survey.id)
            .test()
            .assertValue {
                it.id == survey.id && it.name == survey.name
            }
    }

    @Test
    fun insertAndGetProfileById() {
        val profile = Stubs.PROFILE_ENTITY
        database.appDao().createProfile(profile)

        database.appDao()
            .getProfile(profile.id)
            .test()
            .assertValue {
                it.id == profile.id && it.name == profile.name
            }
    }
}
