package com.petapp.capybara.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.petapp.capybara.R
import com.petapp.capybara.navigation.Screens
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class MainFragment : Fragment(R.layout.fragment_main),
    BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var navigator: Navigator
    private val navigatorHolder by inject<NavigatorHolder>()

    private val viewModel: MainViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {navigator = object : SupportAppNavigator(it, childFragmentManager, R.id.main_container) {} }
        navigatorHolder.setNavigator(navigator)
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        bottom_navigation.selectedItemId = R.id.tab_new_profile
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        activity?.let {
            when (item.itemId) {
                R.id.tab_new_profile -> viewModel.navigateTo(Screens.Profiles)
                R.id.tab_calendar -> viewModel.navigateTo(Screens.Calendar)
                R.id.tab_all_survey -> viewModel.navigateTo(Screens.Surveys)
                R.id.tab_types -> viewModel.navigateTo(Screens.Types)
            }
        }
        return true
    }
}