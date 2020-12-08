package com.petapp.capybara.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import com.petapp.capybara.data.MarksRepository
import com.petapp.capybara.data.SurveysRepository
import com.petapp.capybara.surveys.SurveysViewModel
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
    lateinit var repositorySurveys: SurveysRepository

    @Mock
    lateinit var repositoryMarks: MarksRepository

    @Mock
    lateinit var navController: NavController

    @Test
    fun `should populate surveys and marks LiveData`() {
        val expectedMarks = listOf(
            Stubs.MARK,
            Stubs.MARK,
            Stubs.MARK
        )

        val expectedSurveys = listOf(
            Stubs.SURVEY,
            Stubs.SURVEY,
            Stubs.SURVEY
        )
        `when`(repositoryMarks.getMarks())
            .thenReturn(Single.just(expectedMarks))
        `when`(repositorySurveys.getSurveysByType(Mockito.anyString()))
            .thenReturn(Single.just(expectedSurveys))
        val viewModel = SurveysViewModel(repositorySurveys, repositoryMarks, navController)

        viewModel.getSurveys("ID")
        val surveys = viewModel.surveys.getOrAwaitValue()
        val marks = viewModel.marks.getOrAwaitValue()

        Assert.assertEquals(expectedMarks, marks)
        Assert.assertEquals(expectedSurveys, surveys)
    }

    @Test
    fun `should populate errorMessage LiveData when repository emit error`() {

        val expectedMarks = listOf(
            Stubs.MARK,
            Stubs.MARK,
            Stubs.MARK
        )
        val expected = Stubs.SURVEYS_ERROR
        `when`(repositoryMarks.getMarks())
            .thenReturn(Single.just(expectedMarks))
        `when`(repositorySurveys.getSurveysByType(Mockito.anyString()))
            .thenReturn(Single.error(RuntimeException("Timeout exception")))
        val viewModel = SurveysViewModel(repositorySurveys, repositoryMarks, navController)

        viewModel.getSurveys("ID")
        val value = viewModel.errorMessage.getOrAwaitValue()

        Assert.assertEquals(expected, value)
    }
}
