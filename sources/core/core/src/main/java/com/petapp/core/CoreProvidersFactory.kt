package com.petapp.core

import com.petapp.core_api.context.AppProvider
import com.petapp.core_api.database.DatabaseProvider
import com.petapp.core_api.viewmodel.ViewModelsProvider
import com.petapp.core_impl.database.DaggerDatabaseComponent
import com.petapp.core_impl.viewmodel.DaggerViewModelsComponent

object CoreProvidersFactory {

    fun createDatabaseBuilder(appProvider: AppProvider): DatabaseProvider {
        return DaggerDatabaseComponent.builder().appProvider(appProvider).build()
    }

    fun createViewModelBuilder(): ViewModelsProvider {
        return DaggerViewModelsComponent.create()
    }

}