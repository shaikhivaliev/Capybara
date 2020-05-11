package com.petapp.capybara.di

import com.petapp.capybara.database.DatabaseProvider
import com.petapp.capybara.profiles.data.ProfileEntityMapper
import com.petapp.capybara.profiles.data.ProfilesDataRepository
import com.petapp.capybara.profiles.domain.ProfilesRepository
import com.petapp.capybara.profiles.presentation.ProfilesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { ProfilesViewModel(get()) }

    single<ProfilesRepository> { ProfilesDataRepository(get(), get()) }

    single { DatabaseProvider(androidContext()).appDao() }

    single { ProfileEntityMapper() }

}