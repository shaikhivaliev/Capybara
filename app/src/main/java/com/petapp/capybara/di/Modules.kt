package com.petapp.capybara.di

import com.petapp.capybara.calendar.data.CalendarDataRepository
import com.petapp.capybara.calendar.data.CalendarEntityMapper
import com.petapp.capybara.calendar.domain.CalendarRepository
import com.petapp.capybara.calendar.presentation.CalendarViewModel
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

    single { DatabaseProvider(androidContext()).appDao() }

    viewModel { AppViewModel(get()) }

    viewModel { ProfilesViewModel(get(), get()) }

    viewModel { ProfileViewModel(get(), get()) }

    viewModel { CalendarViewModel(get(), get()) }

    single<ProfileRepository> { ProfileDataRepository(get(), get()) }

    single<CalendarRepository> { CalendarDataRepository(get(), get()) }

    single { ProfileEntityMapper() }

    single { CalendarEntityMapper() }

}

val navigationModule = module {
    val cicerone = Cicerone.create()
    single { cicerone.router }
    single { cicerone.navigatorHolder }
}