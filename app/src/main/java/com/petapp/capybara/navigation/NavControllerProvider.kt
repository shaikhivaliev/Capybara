package com.petapp.capybara.navigation

import androidx.annotation.IdRes
import androidx.navigation.NavController

interface NavControllerProvider {
    fun getNavController(@IdRes viewId: Int): NavController
}
