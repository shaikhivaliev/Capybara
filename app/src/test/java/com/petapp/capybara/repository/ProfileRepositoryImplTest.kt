package com.petapp.capybara.repository

import com.petapp.capybara.data.ProfileRepository
import com.petapp.capybara.data.ProfileRepositoryImpl
import com.petapp.capybara.utils.Stubs
import com.petapp.capybara.data.toProfile
import com.petapp.capybara.database.AppDao
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class ProfileRepositoryImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var appDao: AppDao

    lateinit var repository: ProfileRepository

    @Before
    fun setUp() {
        repository = ProfileRepositoryImpl(appDao)
    }

    @Test
    fun `should emit profile`() {
        val expected = Stubs.PROFILE_ENTITY
        `when`(appDao.getProfile(Mockito.anyString())).thenReturn(Single.just(expected))

        repository.getProfile("ID")
            .test()
            .assertResult(expected.toProfile())
            .assertNoErrors()
    }
}
