package com.petapp.capybara.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.petapp.capybara.R
import com.petapp.capybara.calendar.navigation.CalendarNavigation
import com.petapp.capybara.profile.navigation.ProfilesNavigation
import com.petapp.capybara.setting.navigation.SettingNavigation
import com.petapp.capybara.types.navigation.TypesNavigation

enum class BottomTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: String
) {
    TYPES(
        R.string.tab_list,
        R.drawable.ic_surveys,
        TypesNavigation.route
    ),
    CALENDAR(
        R.string.tab_calendar,
        R.drawable.ic_calendar,
        CalendarNavigation.route
    ),
    PROFILE(
        R.string.tab_new_profile,
        R.drawable.ic_accounts,
        ProfilesNavigation.route
    ),
    SETTING(
        R.string.tab_settings,
        R.drawable.ic_settings,
        SettingNavigation.route
    )
}
