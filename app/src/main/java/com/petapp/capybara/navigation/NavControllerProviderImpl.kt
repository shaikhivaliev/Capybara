package com.petapp.capybara.navigation

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.presentation.main.MainActivity
import javax.inject.Inject

class NavControllerProviderImpl : NavControllerProvider {

    @Inject
    lateinit var activity: MainActivity

    override fun getNavController(@IdRes viewId: Int): NavController {
        FeaturesComponentHolder.featuresComponent?.inject(this)
        return activity.findNavController(viewId)
    }
}
