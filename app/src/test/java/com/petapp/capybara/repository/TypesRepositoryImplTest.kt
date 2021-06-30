package com.petapp.capybara.repository

import com.petapp.capybara.utils.Stubs
import com.petapp.capybara.data.*
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

class TypesRepositoryImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var appDao: AppDao

    private lateinit var repository: ITypesRepository

    @Before
    fun setUp() {
        repository = TypesRepository(appDao)
    }

    @Test
    fun `should emit type`() {
        val expected = Stubs.TYPE_ENTITY
        `when`(appDao.getType(Mockito.anyLong())).thenReturn(Single.just(expected))

        repository.getType(0L)
            .test()
            .assertResult(expected.toType())
            .assertNoErrors()
    }

    @Test
    fun `should emit list of types`() {
        val expected = listOf(
            Stubs.TYPE_SURVEYS,
            Stubs.TYPE_SURVEYS,
            Stubs.TYPE_SURVEYS
        )
        `when`(appDao.getTypesWithSurveys()).thenReturn(Single.just(expected))

        repository.getTypes()
            .test()
            .assertResult(expected.toTypes())
            .assertNoErrors()
    }
}
