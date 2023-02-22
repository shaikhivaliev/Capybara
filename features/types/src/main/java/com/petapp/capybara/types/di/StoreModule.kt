package com.petapp.capybara.types.di

import com.petapp.capybara.types.state.TypesStore
import com.petapp.capybara.types.state.TypesUpdate
import dagger.Module
import dagger.Provides

@Module
class StoreModule {
    @Provides
    fun provideTypesStore(): TypesStore = TypesStore(TypesUpdate())
}
