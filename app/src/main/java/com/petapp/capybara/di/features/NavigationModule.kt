package com.petapp.capybara.di.features

import com.petapp.capybara.navigation.NavControllerProvider
import com.petapp.capybara.navigation.MainNavigatorImpl
import com.petapp.capybara.navigation.NavControllerProviderImpl
import dagger.Module
import dagger.Provides

@Module
class NavigationModule {

    @Provides
    @FeaturesScope
    fun provideNavControllerProvider(): NavControllerProvider = NavControllerProviderImpl()

    @Provides
    @FeaturesScope
    fun provideMainNavigator(
        navControllerProvider: NavControllerProvider
    ): MainNavigatorImpl = MainNavigatorImpl(navControllerProvider)
}
