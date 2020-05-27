package com.petapp.core_impl.viewmodel

import com.petapp.core_api.viewmodel.ViewModelsProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class])
interface ViewModelsComponent : ViewModelsProvider