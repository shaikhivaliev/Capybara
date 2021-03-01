package com.petapp.capybara.di

import androidx.navigation.NavController
import com.petapp.capybara.data.*
import com.petapp.capybara.database.DatabaseProvider
import com.petapp.capybara.presentation.auth.AuthViewModel
import com.petapp.capybara.presentation.calendar.CalendarViewModel
import com.petapp.capybara.presentation.healthDiary.HealthDiaryViewModel
import com.petapp.capybara.presentation.profile.ProfileViewModel
import com.petapp.capybara.presentation.profiles.ProfilesViewModel
import com.petapp.capybara.presentation.survey.SurveyViewModel
import com.petapp.capybara.presentation.surveys.SurveysViewModel
import com.petapp.capybara.presentation.type.TypeViewModel
import com.petapp.capybara.presentation.types.TypesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { DatabaseProvider(androidContext()).appDao() }

    viewModel { AuthViewModel() }

    viewModel { (navController: NavController) ->
        ProfilesViewModel(
            navController = navController,
            repository = get()
        )
    }

    viewModel { (navController: NavController) ->
        ProfileViewModel(
            navController = navController,
            repositoryProfile = get(),
            repositoryHealthDiary = get()
        )
    }

    viewModel { (navController: NavController) ->
        CalendarViewModel(
            navController = navController,
            repositoryMark = get(),
            repositorySurveys = get()
        )
    }

    viewModel { (navController: NavController) ->
        TypesViewModel(
            navController = navController,
            repository = get()
        )
    }

    viewModel { (navController: NavController) ->
        TypeViewModel(
            navController = navController,
            repository = get()
        )
    }

    viewModel { (navController: NavController, typeId: Long) ->
        SurveysViewModel(
            navController = navController,
            repositoryMarks = get(),
            repositorySurveys = get(),
            typeId = typeId
        )
    }

    viewModel { (navController: NavController) ->
        SurveyViewModel(
            navController = navController,
            repositorySurveys = get(),
            repositoryTypes = get(),
            repositoryMarks = get()
        )
    }

    viewModel { (navController: NavController) ->
        HealthDiaryViewModel(
            repositoryHealthDiary = get(),
            repositoryMarks = get(),
            navController = navController
        )
    }

    single<MarksRepository> { MarksRepositoryImpl(get()) }

    single<ProfileRepository> { ProfileRepositoryImpl(get()) }

    single<TypesRepository> { TypesRepositoryImpl(get()) }

    single<SurveysRepository> { SurveysRepositoryImpl(get()) }

    single<HealthDiaryRepository> { HealthDiaryRepositoryImpl(get()) }
}
