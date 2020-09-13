package com.petapp.capybara

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.petapp.capybara.EntityTestHelper.Companion.PROFILE
import com.petapp.capybara.EntityTestHelper.Companion.SURVEY
import com.petapp.capybara.EntityTestHelper.Companion.TYPE
import com.petapp.capybara.database.AppDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class EntityReadWriteTest {

    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase

    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java,
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
        database.appDao().createType(TYPE)

        database.appDao()
            .getType(TYPE.id.toString())
            .test()
            .assertValue { type ->
                type.id == TYPE.id && type.name == TYPE.name
            }
    }

    @Test
    fun insertAndGetSurveyById() {
        database.appDao().createSurvey(SURVEY)

        database.appDao()
            .getSurvey(SURVEY.id.toString())
            .test()
            .assertValue { survey ->
                survey.id == SURVEY.id && survey.name == SURVEY.name
            }
    }

    @Test
    fun insertAndGetProfileById() {
        database.appDao().createProfile(PROFILE)

        database.appDao()
            .getProfile(PROFILE.id.toString())
            .test()
            .assertValue { profile ->
                profile.id == PROFILE.id && profile.name == PROFILE.name
            }
    }
}
