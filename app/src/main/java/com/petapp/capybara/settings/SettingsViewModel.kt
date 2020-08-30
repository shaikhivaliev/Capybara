package com.petapp.capybara.settings

import androidx.annotation.StringRes
import androidx.navigation.NavController
import com.petapp.capybara.BaseViewModel
import com.petapp.capybara.R
import com.petapp.capybara.extensions.navigateWith

class SettingsViewModel(
    private val navController: NavController
) : BaseViewModel() {

    fun openSettingsScreen(nameRes: Int) {
        when (nameRes) {
            Settings.FEEDBACK.value -> SettingFragmentDirections.toFeedback().navigateWith(navController)
            Settings.ABOUT_APP.value -> SettingFragmentDirections.toAboutApp().navigateWith(navController)
            Settings.RULES.value -> SettingFragmentDirections.toRules().navigateWith(navController)
        }
    }

    fun exit() {
        SettingFragmentDirections.exit().navigateWith(navController)
    }

    enum class Settings(@StringRes val value: Int) {
        FEEDBACK(value = R.string.settings_feedback),
        ABOUT_APP(value = R.string.settings_about_app),
        RULES(value = R.string.settings_rules)
    }
}