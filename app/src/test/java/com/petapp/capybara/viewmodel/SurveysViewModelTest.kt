package com.petapp.capybara.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import com.petapp.capybara.data.IProfileRepository
import com.petapp.capybara.data.ISurveysRepository
import com.petapp.capybara.presentation.surveys.SurveysVm
import com.petapp.capybara.utils.RxRule
import com.petapp.capybara.utils.Stubs
import com.petapp.capybara.utils.getOrAwaitValue
import io.reactivex.Single
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class SurveysViewModelTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val rxRule: RxRule = RxRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repositorySurveys: ISurveysRepository

    @Mock
    lateinit var repositoryProfile: IProfileRepository

    @Mock
    lateinit var navController: NavController

    @Test
    fun `should populate profiles LiveData`() {
        val expectedMarks = listOf(
            Stubs.PROFILE,
            Stubs.PROFILE,
            Stubs.PROFILE
        )

        val expectedSurveys = listOf(
            Stubs.SURVEY,
            Stubs.SURVEY,
            Stubs.SURVEY
        )
        `when`(repositoryProfile.getProfiles())
            .thenReturn(Single.just(expectedMarks))
        `when`(repositorySurveys.getSurveysByType(Mockito.anyLong()))
            .thenReturn(Single.just(expectedSurveys))
        val viewModel = SurveysVm(repositorySurveys, repositoryProfile, navController, 0L)

        viewModel.getSurveys()
        val marks = viewModel.profiles.getOrAwaitValue()

        Assert.assertEquals(expectedMarks, marks)
    }

    @Test
    fun `should populate errorMessage LiveData when repository emit error`() {

        val expectedProfiles = listOf(
            Stubs.PROFILE,
            Stubs.PROFILE,
            Stubs.PROFILE
        )
        val expected = Stubs.SURVEYS_ERROR
        `when`(repositoryProfile.getProfiles())
            .thenReturn(Single.just(expectedProfiles))
        `when`(repositorySurveys.getSurveysByType(Mockito.anyLong()))
            .thenReturn(Single.error(RuntimeException("Timeout exception")))
        val viewModel = SurveysVm(repositorySurveys, repositoryProfile, navController, 0L)

        viewModel.getSurveys()
        val value = viewModel.errorMessage.getOrAwaitValue()

        Assert.assertEquals(expected, value)
    }
}
