package com.petapp.main.presentation

import androidx.lifecycle.ViewModel
import com.petapp.core_api.navigation.FragmentProvider
import ru.terrakok.cicerone.Router
import javax.inject.Inject
import javax.inject.Named

class MainViewModel @Inject constructor(
    private val router: Router,
    @field:Named("main")
    private val fragmentProvider: FragmentProvider
) : ViewModel() {

    fun openMainFragment() {
        router.navigateTo(fragmentProvider.createScreen())
    }

    fun openProfileTab() {

    }

    fun openCalendarTab() {
    }

    fun openSurveysTab() {
    }


}