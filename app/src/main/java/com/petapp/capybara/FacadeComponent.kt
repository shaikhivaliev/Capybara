package com.petapp.capybara

import android.app.Application
import com.petapp.core.CoreProvidersFactory.createDatabaseBuilder
import com.petapp.core.CoreProvidersFactory.createNavigationBuilder
import com.petapp.core_api.ProvidersFacade
import com.petapp.core_api.context.AppProvider
import com.petapp.core_api.database.DatabaseProvider
import com.petapp.core_api.navigation.NavigationProvider
import dagger.Component
import javax.inject.Singleton

@Component(
    dependencies = [AppProvider::class, DatabaseProvider::class, NavigationProvider::class],
    modules = [NavigationMediator::class]
)
interface FacadeComponent : ProvidersFacade {

    companion object {

        fun init(application: Application): FacadeComponent =
            DaggerFacadeComponent
                .builder()
                .appProvider(AppComponent.create(application))
                .databaseProvider(createDatabaseBuilder(AppComponent.create(application)))
                .navigationProvider(createNavigationBuilder())
                .build()
    }

    fun inject(app: App)

}