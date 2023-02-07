package com.petapp.capybara.di.features

import com.petapp.capybara.core.navigation.IMainNavigator
import com.petapp.capybara.core.navigation.INavControllerProvider
import com.petapp.capybara.core.navigation.MainNavigator
import com.petapp.capybara.core.navigation.NavControllerProvider
import dagger.Module
import dagger.Provides

@Module
class NavigationModule {

    @Provides
    @FeaturesScope
    fun provideNavControllerProvider(): INavControllerProvider = NavControllerProvider()

    @Provides
    @FeaturesScope
    fun provideMainNavigator(
        navControllerProvider: INavControllerProvider
    ): IMainNavigator = MainNavigator(navControllerProvider)
}
