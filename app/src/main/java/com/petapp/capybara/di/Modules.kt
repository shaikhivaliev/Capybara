package com.petapp.capybara.di

import com.petapp.capybara.database.DatabaseProvider
import com.petapp.capybara.main.AppViewModel
import com.petapp.capybara.main.MainViewModel
import com.petapp.capybara.profiles.data.ProfileEntityMapper
import com.petapp.capybara.profiles.data.ProfilesDataRepository
import com.petapp.capybara.profiles.domain.ProfilesRepository
import com.petapp.capybara.profiles.presentation.ProfilesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone

val appModule = module {

    viewModel { ProfilesViewModel(get()) }

    viewModel { AppViewModel(get()) }

    viewModel { MainViewModel(get()) }

    single<ProfilesRepository> { ProfilesDataRepository(get(), get()) }

    single { DatabaseProvider(androidContext()).appDao() }

    single { ProfileEntityMapper() }

}

val navigationModule = module {
    val cicerone = Cicerone.create()
    single { cicerone.router }
    single { cicerone.navigatorHolder }
}