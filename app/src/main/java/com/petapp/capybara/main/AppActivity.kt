package com.petapp.capybara.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.petapp.capybara.R
import com.petapp.capybara.navigation.Screens
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class AppActivity : AppCompatActivity(R.layout.layout_container) {

    private val navigator: Navigator = object : SupportAppNavigator(this, supportFragmentManager, R.id.container) {}
    private val navigatorHolder by inject<NavigatorHolder>()
    private lateinit var viewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
        viewModel.setRootScreen(Screens.Main)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

}
