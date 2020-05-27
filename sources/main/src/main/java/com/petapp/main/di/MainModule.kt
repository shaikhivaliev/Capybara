package com.petapp.main.di

import androidx.lifecycle.ViewModel
import com.petapp.main.presentation.MainViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule {

    @Provides
    @Singleton
    fun provideMainViewModel(
        map: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>
    ): ViewModel = MainViewModel().also {
        map[MainViewModel::class.java] = it
    }

}

