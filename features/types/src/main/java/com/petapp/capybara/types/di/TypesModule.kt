package com.petapp.capybara.types.di

import com.petapp.capybara.core.data.repository.TypesRepository
import com.petapp.capybara.types.state.TypesStore
import com.petapp.capybara.types.presentation.TypesVm
import dagger.Module
import dagger.Provides

@Module
class TypesModule {

    @Provides
    @TypesScope
    fun provideTypesVmFactory(
        typesRepository: TypesRepository,
        store: TypesStore
    ): TypesVm = TypesVm(typesRepository, store)
}
