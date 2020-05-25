package com.petapp.capybara.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.petapp.capybara.R
import com.petapp.capybara.common.App
import com.petapp.capybara.navigation.Screens
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject

class AppActivity : AppCompatActivity(R.layout.activity_container) {

    private val navigator: Navigator by lazy { object : SupportAppNavigator(this, supportFragmentManager, R.id.container) {} }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, factory).get(AppViewModel::class.java)

        viewModel.setRootScreen(Screens.Main)
    }


    override fun onResumeFragments() {
        navigatorHolder.setNavigator(navigator)
        super.onResumeFragments()
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

}
