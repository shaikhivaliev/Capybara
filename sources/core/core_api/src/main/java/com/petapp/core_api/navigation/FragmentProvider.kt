package com.petapp.core_api.navigation

import ru.terrakok.cicerone.Screen

interface FragmentProvider {

    fun createScreen(): Screen

}
