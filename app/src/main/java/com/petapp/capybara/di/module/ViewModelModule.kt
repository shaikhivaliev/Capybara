package com.petapp.capybara.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.petapp.capybara.di.ViewModelFactory
import com.petapp.capybara.di.ViewModelKey
import com.petapp.capybara.profiles.presentation.profile.ProfileViewModel
import com.petapp.capybara.profiles.presentation.profiles.ProfilesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ProfilesViewModel::class)
    internal abstract fun bindProfilesViewModel(viewModel: ProfilesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfilesViewModel::class)
    internal abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel


    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}