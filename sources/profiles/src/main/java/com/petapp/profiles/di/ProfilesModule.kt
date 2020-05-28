package com.petapp.profiles.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.petapp.profiles.data.ProfileDataRepository
import com.petapp.profiles.data.ProfileEntityMapper
import com.petapp.profiles.domain.ProfileRepository
import com.petapp.profiles.presentation.profile.ProfileViewModel
import com.petapp.profiles.presentation.profiles.ProfilesViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module(includes = [ProfilesModule.RepositoryBinds::class, ProfilesModule.ViewModelsBinds::class])
class ProfilesModule {

    @Provides
    @Singleton
    fun provideProfileViewModel(
        map: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>,
        repository: ProfileRepository
    ): ViewModel = ProfileViewModel(repository).also {
        map[ProfileViewModel::class.java] = it
    }

    @Provides
    @Singleton
    fun provideProfilesViewModel(
        map: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>,
        repository: ProfileRepository
    ): ViewModel = ProfilesViewModel(repository).also {
        map[ProfilesViewModel::class.java] = it
    }

    @Singleton
    @Provides
    fun providersProfilesMapper() = ProfileEntityMapper()

    @Module
    interface RepositoryBinds {
        @Binds
        fun bindsProfilesRepository(repository: ProfileDataRepository): ProfileRepository
    }

    @Module
    interface ViewModelsBinds {
        @Binds
        @IntoMap
        @ViewModelKey(ProfileViewModel::class)
        fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(ProfilesViewModel::class)
        fun bindProfilesViewModel(viewModel: ProfilesViewModel): ViewModel


        @Binds
        fun bindFactory(factory: ViewModelFactory): ViewModelProvider.Factory
    }

}