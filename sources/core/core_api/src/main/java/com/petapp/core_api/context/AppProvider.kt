package com.petapp.core_api.context

import android.content.Context

interface AppProvider {

    fun provideApplication(): Context

}