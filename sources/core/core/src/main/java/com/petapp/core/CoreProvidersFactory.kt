package com.petapp.core

import com.petapp.core_api.context.AppProvider
import com.petapp.core_api.database.DatabaseProvider
import com.petapp.core_api.navigation.NavigationProvider
import com.petapp.core_impl.database.DaggerDatabaseComponent
import com.petapp.core_impl.navigation.DaggerNavigationComponent

object CoreProvidersFactory {

    fun createDatabaseBuilder(appProvider: AppProvider): DatabaseProvider {
        return DaggerDatabaseComponent.builder().appProvider(appProvider).build()
    }

    fun createNavigationBuilder(): NavigationProvider {
        return DaggerNavigationComponent.builder().build()
    }
}