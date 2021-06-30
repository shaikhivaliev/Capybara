package com.petapp.capybara.core.navigation

import androidx.annotation.IdRes
import androidx.navigation.NavController

interface INavControllerProvider {
    fun getNavController(@IdRes viewId: Int): NavController
}
