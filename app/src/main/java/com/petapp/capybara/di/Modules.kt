package com.petapp.capybara.di

import com.petapp.capybara.calendar.CalendarViewModel
import com.petapp.capybara.data.*
import com.petapp.capybara.database.DatabaseProvider
import com.petapp.capybara.profile.ProfileViewModel
import com.petapp.capybara.profiles.ProfilesViewModel
import com.petapp.capybara.survey.SurveyViewModel
import com.petapp.capybara.surveys.SurveysViewModel
import com.petapp.capybara.type.TypeViewModel
import com.petapp.capybara.types.TypesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { DatabaseProvider(androidContext()).appDao() }

    viewModel { ProfilesViewModel(get()) }

    viewModel { ProfileViewModel(get()) }

    viewModel { CalendarViewModel(get()) }

    viewModel { TypesViewModel(get()) }

    viewModel { TypeViewModel(get()) }

    viewModel { SurveysViewModel(get(), get()) }

    viewModel { SurveyViewModel(get(), get()) }

    single<MarksRepository> { MarksRepositoryImpl(get()) }

    single<ProfileRepository> { ProfileRepositoryImpl(get()) }

    single<TypesRepository> { TypesRepositoryImpl(get()) }

    single<SurveysRepository> { SurveysRepositoryImpl(get()) }
}
