package com.petapp.core_impl.navigation

import com.petapp.core_api.navigation.NavigationProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NavigationModule::class])
interface NavigationComponent : NavigationProvider