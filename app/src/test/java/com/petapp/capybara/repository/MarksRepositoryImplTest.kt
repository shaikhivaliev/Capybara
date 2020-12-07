package com.petapp.capybara.repository

import com.petapp.capybara.data.MarksRepository
import com.petapp.capybara.data.MarksRepositoryImpl
import com.petapp.capybara.utils.Stubs
import com.petapp.capybara.data.toMarks
import com.petapp.capybara.database.AppDao
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class MarksRepositoryImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var appDao: AppDao

    lateinit var repository: MarksRepository

    @Test
    fun `should emit marks list of marks`() {
        val expected = listOf(
            Stubs.PROFILE_ENTITY,
            Stubs.PROFILE_ENTITY,
            Stubs.PROFILE_ENTITY
        )
        repository = MarksRepositoryImpl(appDao)
        `when`(appDao.getProfiles()).thenReturn(Single.just(expected))

        repository.getMarks()
            .test()
            .assertResult(expected.toMarks())
            .assertNoErrors()
    }
}
