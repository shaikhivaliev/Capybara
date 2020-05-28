package com.petapp.core_api.navigation

import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

interface NavigationProvider {

    fun provideCicerone(): Cicerone<Router>

    fun provideRouter(): Router

    fun provideNavigatorHolder(): NavigatorHolder

}