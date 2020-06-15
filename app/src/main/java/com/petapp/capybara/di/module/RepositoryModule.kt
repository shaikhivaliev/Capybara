package com.petapp.capybara.di.module

import com.petapp.capybara.profiles.data.ProfilesDataRepository
import com.petapp.capybara.profiles.domain.ProfilesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RepositoryModule.RepositoryBinds::class])
class RepositoryModule {

    @Module
    interface RepositoryBinds {
        @Binds
        fun bindsProfilesRepository(repository: ProfilesDataRepository): ProfilesRepository
    }

}