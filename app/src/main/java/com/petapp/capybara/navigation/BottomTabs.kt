package com.petapp.capybara.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.petapp.capybara.R
import com.petapp.capybara.calendar.navigation.CalendarNavigationScreen
import com.petapp.capybara.profile.navigation.ProfilesNavigationScreen
import com.petapp.capybara.setting.navigation.SettingNavigationScreen
import com.petapp.capybara.types.navigation.TypesNavigationScreen

enum class BottomTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: String
) {
    TYPES(
        R.string.tab_list,
        R.drawable.ic_surveys,
        TypesNavigationScreen.route
    ),
    CALENDAR(
        R.string.tab_calendar,
        R.drawable.ic_calendar,
        CalendarNavigationScreen.route
    ),
    PROFILE(
        R.string.tab_new_profile,
        R.drawable.ic_accounts,
        ProfilesNavigationScreen.route
    ),
    SETTING(
        R.string.tab_settings,
        R.drawable.ic_settings,
        SettingNavigationScreen.route
    )
}
