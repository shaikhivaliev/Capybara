package com.petapp.capybara.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import com.petapp.capybara.data.ProfileRepository
import com.petapp.capybara.presentation.profiles.ProfilesViewModel
import com.petapp.capybara.utils.RxRule
import com.petapp.capybara.utils.Stubs
import com.petapp.capybara.utils.getOrAwaitValue
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class ProfilesViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val rxRule: RxRule = RxRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: ProfileRepository

    @Mock
    lateinit var navController: NavController

    private lateinit var viewModel: ProfilesViewModel

    @Before
    fun setUp() {
        viewModel = ProfilesViewModel(repository, navController)
    }

    @Test
    fun `should populate profiles LiveData`() {
        val expected = listOf(
            Stubs.PROFILE,
            Stubs.PROFILE,
            Stubs.PROFILE
        )
        `when`(repository.getProfiles())
            .thenReturn(Single.just(expected))

        viewModel.getProfiles()
        val value = viewModel.profiles.getOrAwaitValue()

        Assert.assertEquals(expected, value)
    }

    @Test
    fun `should populate errorMessage LiveData when repository emit error`() {
        val expected = Stubs.PROFILES_ERROR
        `when`(repository.getProfiles())
            .thenReturn(Single.error(RuntimeException("Timeout exception")))

        viewModel.getProfiles()
        val value = viewModel.errorMessage.getOrAwaitValue()

        Assert.assertEquals(expected, value)
    }
}
