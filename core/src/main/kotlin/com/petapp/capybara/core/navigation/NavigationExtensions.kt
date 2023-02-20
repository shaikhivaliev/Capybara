package com.petapp.capybara.core

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.petapp.capybara.core.navigation.NavigationScreen

fun NavController.navigateTo(screen: NavigationScreen, navOptions: NavOptions? = null) {
    this.navigate(screen.route, navOptions)
}
