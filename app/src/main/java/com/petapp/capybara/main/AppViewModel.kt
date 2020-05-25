package com.petapp.capybara.main

import com.petapp.capybara.common.BaseViewModel
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import javax.inject.Inject

class AppViewModel @Inject constructor(
    private val router: Router
) : BaseViewModel() {

    fun setRootScreen(screen: Screen) {
        router.newRootScreen(screen)
    }
}