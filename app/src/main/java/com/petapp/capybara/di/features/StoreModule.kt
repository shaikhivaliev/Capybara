package com.petapp.capybara.di.features

import com.petapp.capybara.presentation.types.TypesStore
import com.petapp.capybara.presentation.types.TypesUpdate
import dagger.Module
import dagger.Provides

@Module
class StoreModule {
    @Provides
    fun provideTypesStore(): TypesStore = TypesStore(TypesUpdate())
}
