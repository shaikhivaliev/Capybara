package com.petapp.capybara.types.di

import com.petapp.capybara.types.TypesStore
import com.petapp.capybara.types.TypesUpdate
import dagger.Module
import dagger.Provides

@Module
class StoreModule {
    @Provides
    fun provideTypesStore(): TypesStore = TypesStore(TypesUpdate())
}
