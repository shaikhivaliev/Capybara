package com.petapp.capybara.di

import com.petapp.capybara.calendar.CalendarViewModel
import com.petapp.capybara.common.data.CommonDataRepository
import com.petapp.capybara.common.data.CommonEntityMapper
import com.petapp.capybara.common.domain.CommonRepository
import com.petapp.capybara.database.DatabaseProvider
import com.petapp.capybara.profiles.data.ProfileDataRepository
import com.petapp.capybara.profiles.data.ProfileEntityMapper
import com.petapp.capybara.profiles.domain.ProfileRepository
import com.petapp.capybara.profiles.presentation.profile.ProfileViewModel
import com.petapp.capybara.profiles.presentation.profiles.ProfilesViewModel
import com.petapp.capybara.surveys.data.SurveysDataRepository
import com.petapp.capybara.surveys.data.TypesDataRepository
import com.petapp.capybara.surveys.data.mappers.SurveyEntityMapper
import com.petapp.capybara.surveys.data.mappers.TypesEntityMapper
import com.petapp.capybara.surveys.domain.SurveysRepository
import com.petapp.capybara.surveys.domain.TypesRepository
import com.petapp.capybara.surveys.presentation.survey.SurveyViewModel
import com.petapp.capybara.surveys.presentation.surveys.SurveysViewModel
import com.petapp.capybara.surveys.presentation.type.TypeViewModel
import com.petapp.capybara.surveys.presentation.types.TypesViewModel
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

    viewModel { SurveyViewModel(get()) }

    single<CommonRepository> { CommonDataRepository(get(), get()) }

    single<ProfileRepository> { ProfileDataRepository(get(), get()) }

    single<TypesRepository> { TypesDataRepository(get(), get()) }

    single<SurveysRepository> { SurveysDataRepository(get(), get()) }

    single { ProfileEntityMapper() }

    single { CommonEntityMapper() }

    single { SurveyEntityMapper() }

    single { TypesEntityMapper() }
}
