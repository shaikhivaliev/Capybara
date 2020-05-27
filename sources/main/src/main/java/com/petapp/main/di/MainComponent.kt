package com.petapp.main.di

import com.petapp.core.CoreProvidersFactory
import com.petapp.core_api.ProvidersFacade
import com.petapp.core_api.viewmodel.ViewModelsProvider
import com.petapp.main.presentation.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [MainModule::class],
    dependencies = [ProvidersFacade::class, ViewModelsProvider::class]
)
interface MainComponent : ViewModelsProvider {

    companion object {
        fun create(providersFacade: ProvidersFacade): MainComponent {
            return DaggerMainComponent
                .builder()
                .viewModelsProvider(CoreProvidersFactory.createViewModelBuilder())
                .providersFacade(providersFacade)
                .build()
        }
    }

    fun inject(mainActivity: MainActivity)
}