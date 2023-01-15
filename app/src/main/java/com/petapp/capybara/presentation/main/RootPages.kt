package com.petapp.capybara.presentation.main

import com.petapp.capybara.R
import com.petapp.capybara.core.navigation.BaseScreen
import com.petapp.capybara.core.navigation.Screens

enum class RootPages(
    val menuId: Int,
    val screen: BaseScreen
) {
    Types(
        R.id.tab_types,
        Screens.TypesScreen()
    ),
    Calendar(
        R.id.tab_calendar,
        Screens.CalendarScreen()
    ),
    Profiles(
        R.id.tab_profiles,
        Screens.ProfilesScreen()
    ),
    Settings(
        R.id.tab_settings,
        Screens.SettingsScreen()
    );

    val screenKey
        get() = screen.screenKey

    companion object {
        fun byMenuId(id: Int) = values().find { it.menuId == id }
        fun byScreenKey(key: String?) =
            values().find { it.screenKey == key }
    }
}
