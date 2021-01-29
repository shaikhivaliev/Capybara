package com.petapp.capybara.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import com.petapp.capybara.data.TypesRepository
import com.petapp.capybara.presentation.types.TypesViewModel
import com.petapp.capybara.utils.RxRule
import com.petapp.capybara.utils.Stubs
import com.petapp.capybara.utils.getOrAwaitValue
import io.reactivex.Single
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class TypesViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val rxRule: RxRule = RxRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: TypesRepository

    @Mock
    lateinit var navController: NavController

    @Test
    fun `should populate types LiveData`() {
        val expected = listOf(
            Stubs.TYPE,
            Stubs.TYPE,
            Stubs.TYPE
        )
        Mockito.`when`(repository.getTypes())
            .thenReturn(Single.just(expected))

        val viewModel = TypesViewModel(repository, navController)
        val value = viewModel.types.getOrAwaitValue()

        Assert.assertEquals(expected, value)
    }

    @Test
    fun `should populate errorMessage LiveData when repository emit error`() {
        val expected = Stubs.TYPES_ERROR
        Mockito.`when`(repository.getTypes())
            .thenReturn(Single.error(RuntimeException("Timeout exception")))

        val viewModel = TypesViewModel(repository, navController)
        val value = viewModel.errorMessage.getOrAwaitValue()

        Assert.assertEquals(expected, value)
    }
}
