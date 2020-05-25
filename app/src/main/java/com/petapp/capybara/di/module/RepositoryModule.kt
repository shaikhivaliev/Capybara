package com.petapp.capybara.di.module

import com.petapp.capybara.profiles.data.ProfileDataRepository
import com.petapp.capybara.profiles.data.ProfileEntityMapper
import com.petapp.capybara.profiles.domain.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RepositoryModule.RepositoryBinds::class])
class RepositoryModule {
    @Singleton
    @Provides
    fun providersProfilesMapper() = ProfileEntityMapper()

    @Module
    interface RepositoryBinds {
        @Binds
        fun bindsProfilesRepository(repository: ProfileDataRepository): ProfileRepository
    }
}