package com.petapp.capybara.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.petapp.capybara.R
import com.petapp.capybara.navigation.Screens
import kotlinx.android.synthetic.main.fragment_main.*
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MainFragment : Fragment(R.layout.fragment_main),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private val currentTabFragment: Fragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        bottom_navigation.selectedItemId = R.id.tab_profiles
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (!item.isChecked) {
            when (item.itemId) {
                R.id.tab_profiles -> selectTab(Screens.Profiles)
                R.id.tab_calendar -> selectTab(Screens.Calendar)
                R.id.tab_surveys -> selectTab(Screens.Surveys)
            }
        }
        return true
    }

    private fun selectTab(tab: SupportAppScreen) {
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(tab.screenKey)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return

        childFragmentManager.beginTransaction().apply {
            val fragment = tab.fragment
            if (newFragment == null && fragment != null) add(R.id.main_container, fragment, tab.screenKey)

            currentFragment?.let {
                hide(it)
                setMaxLifecycle(it, Lifecycle.State.STARTED)
            }
            newFragment?.let {
                show(it)
                setMaxLifecycle(it, Lifecycle.State.RESUMED)
            }
        }.commitNow()
    }

}