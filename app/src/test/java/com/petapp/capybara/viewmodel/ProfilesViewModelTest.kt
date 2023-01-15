package com.petapp.capybara.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.petapp.capybara.core.navigation.IMainCoordinator
import com.petapp.capybara.data.IProfileRepository
import com.petapp.capybara.presentation.profiles.ProfilesVm
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
    lateinit var savedStateHandle: SavedStateHandle

    @Mock
    lateinit var repository: IProfileRepository

    @Mock
    lateinit var mainNavigator: IMainCoordinator

    private lateinit var viewModel: ProfilesVm

    @Before
    fun setUp() {
        viewModel = ProfilesVm(
            savedStateHandle = savedStateHandle,
            profileRepository = repository,
            mainNavigator = mainNavigator
        )
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
