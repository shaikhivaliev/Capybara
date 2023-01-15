package com.petapp.capybara.core.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen

abstract class BaseScreen : Screen {

    open var args: Bundle? = null

    abstract fun getFragmentInstance(): Fragment

    fun createFragment(): Fragment {
        val args = args ?: Bundle()
        val fragment = getFragmentInstance()
        fragment.arguments = (fragment.arguments ?: Bundle()).apply {
            putAll(args)
        }
        return fragment
    }
}

fun BaseScreen.toFragmentScreen(): FragmentScreen {
    return FragmentScreen(key = this.screenKey) {
        this.createFragment()
    }
}

