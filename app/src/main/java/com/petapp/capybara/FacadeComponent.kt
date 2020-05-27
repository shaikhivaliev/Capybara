package com.petapp.capybara

import android.app.Application
import com.petapp.core.CoreProvidersFactory
import com.petapp.core.CoreProvidersFactory.createDatabaseBuilder
import com.petapp.core.CoreProvidersFactory.createViewModelBuilder
import com.petapp.core_api.ProvidersFacade
import com.petapp.core_api.context.AppProvider
import com.petapp.core_api.database.DatabaseProvider
import com.petapp.core_api.viewmodel.ViewModelsProvider
import dagger.Component


@Component(dependencies = [AppProvider::class, DatabaseProvider::class])
interface FacadeComponent : ProvidersFacade {

    companion object {

        fun init(application: Application): FacadeComponent =
            DaggerFacadeComponent
                .builder()
                .appProvider(AppComponent.create(application))
                .databaseProvider(createDatabaseBuilder(AppComponent.create(application)))
                .build()
    }

    fun inject(app: App)

}