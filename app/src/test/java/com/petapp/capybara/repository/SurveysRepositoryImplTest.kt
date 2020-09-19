package com.petapp.capybara.repository

import com.petapp.capybara.data.SurveysRepository
import com.petapp.capybara.data.SurveysRepositoryImpl
import com.petapp.capybara.data.toSurvey
import com.petapp.capybara.data.toSurveys
import com.petapp.capybara.database.AppDao
import com.petapp.capybara.utils.Stubs
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class SurveysRepositoryImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var appDao: AppDao

    lateinit var repository: SurveysRepository

    @Before
    fun setUp() {
        repository = SurveysRepositoryImpl(appDao)
    }

    @Test
    fun `should emit survey`() {
        val expected = Stubs.SURVEY_ENTITY
        `when`(appDao.getSurvey(Mockito.anyString())).thenReturn(Single.just(expected))

        repository.getSurvey("ID")
            .test()
            .assertResult(expected.toSurvey())
            .assertNoErrors()
    }

    @Test
    fun `should emit list of survey`() {
        val expected = listOf(
            Stubs.SURVEY_ENTITY,
            Stubs.SURVEY_ENTITY,
            Stubs.SURVEY_ENTITY
        )
        `when`(appDao.getSurveys(Mockito.anyString())).thenReturn(Single.just(expected))

        repository.getSurveys("ID")
            .test()
            .assertResult(expected.toSurveys())
            .assertNoErrors()
    }
}
