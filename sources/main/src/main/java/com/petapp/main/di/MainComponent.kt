package com.petapp.main.di

import com.petapp.core_api.ProvidersFacade
import com.petapp.main.presentation.MainActivity
import com.petapp.main.presentation.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [MainModule::class],
    dependencies = [ProvidersFacade::class]
)
interface MainComponent {

    companion object {
        fun create(providersFacade: ProvidersFacade): MainComponent {
            return DaggerMainComponent
                .builder()
                .providersFacade(providersFacade)
                .build()
        }
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: MainFragment)
}