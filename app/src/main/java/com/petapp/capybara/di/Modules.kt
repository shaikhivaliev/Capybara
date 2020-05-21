package com.petapp.capybara.di

import com.petapp.capybara.database.DatabaseProvider
import com.petapp.capybara.main.AppViewModel
import com.petapp.capybara.profiles.data.ProfileEntityMapper
import com.petapp.capybara.profiles.data.ProfileDataRepository
import com.petapp.capybara.profiles.domain.ProfileRepository
import com.petapp.capybara.profiles.presentation.profile.ProfileViewModel
import com.petapp.capybara.profiles.presentation.profiles.ProfilesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone

val appModule = module {

    viewModel { AppViewModel(get()) }

    viewModel { ProfilesViewModel(get(), get()) }

    viewModel { ProfileViewModel(get(), get()) }

    single<ProfileRepository> { ProfileDataRepository(get(), get()) }

    single { DatabaseProvider(androidContext()).appDao() }

    single { ProfileEntityMapper() }

}

val navigationModule = module {
    val cicerone = Cicerone.create()
    single { cicerone.router }
    single { cicerone.navigatorHolder }
}