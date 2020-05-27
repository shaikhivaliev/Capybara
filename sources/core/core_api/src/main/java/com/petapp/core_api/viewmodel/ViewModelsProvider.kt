package com.petapp.core_api.viewmodel

import androidx.lifecycle.ViewModelProvider

interface ViewModelsProvider {

    fun provideViewModels(): ViewModelProvider.Factory

}