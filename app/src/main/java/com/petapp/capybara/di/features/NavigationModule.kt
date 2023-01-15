package com.petapp.capybara.di.features

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.petapp.capybara.core.navigation.AppRouter
import com.petapp.capybara.core.navigation.IMainCoordinator
import com.petapp.capybara.core.navigation.MainCoordinator
import dagger.Module
import dagger.Provides

@Module
class NavigationModule {

    private val cicerone: Cicerone<AppRouter> = Cicerone.create(AppRouter())

    @Provides
    fun provideRouter(): AppRouter = cicerone.router

    @Provides
    @FeaturesScope
    fun provideNavigatorHolder(): NavigatorHolder = cicerone.getNavigatorHolder()

    @Provides
    @FeaturesScope
    fun provideMainCoordinator(): IMainCoordinator = MainCoordinator(cicerone.router)
}
