package com.petapp.capybara.main

import com.petapp.capybara.common.BaseViewModel
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen

class MainViewModel(private val router: Router) : BaseViewModel() {

    fun navigateTo(screen: Screen) {
        router.navigateTo(screen)
    }
}