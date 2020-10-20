package com.petapp.capybara.di

import androidx.navigation.NavController
import com.petapp.capybara.auth.AuthViewModel
import com.petapp.capybara.calendar.CalendarViewModel
import com.petapp.capybara.data.*
import com.petapp.capybara.database.DatabaseProvider
import com.petapp.capybara.profile.ProfileViewModel
import com.petapp.capybara.profiles.ProfilesViewModel
import com.petapp.capybara.settings.SettingsViewModel
import com.petapp.capybara.survey.SurveyViewModel
import com.petapp.capybara.surveys.SurveysViewModel
import com.petapp.capybara.type.TypeViewModel
import com.petapp.capybara.types.TypesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { DatabaseProvider(androidContext()).appDao() }

    viewModel { (navController: NavController) ->
        AuthViewModel(
            navController = navController
        )
    }

    viewModel { (navController: NavController) ->
        ProfilesViewModel(
            navController = navController,
            repository = get()
        )
    }

    viewModel { (navController: NavController) ->
        ProfileViewModel(
            navController = navController,
            repository = get()
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

    viewModel { (navController: NavController) ->
        SurveysViewModel(
            navController = navController,
            repositoryMarks = get(),
            repositorySurveys = get()
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
        SettingsViewModel(
            navController = navController
        )
    }

    single<MarksRepository> { MarksRepositoryImpl(get()) }

    single<ProfileRepository> { ProfileRepositoryImpl(get()) }

    single<TypesRepository> { TypesRepositoryImpl(get()) }

    single<SurveysRepository> { SurveysRepositoryImpl(get()) }
}
