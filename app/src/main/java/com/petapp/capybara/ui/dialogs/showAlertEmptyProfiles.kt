package com.petapp.capybara.ui.dialogs

import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.petapp.capybara.R

fun Fragment.showAlertEmptyProfiles(onClick: () -> Unit) {
    MaterialDialog(requireActivity())
        .cancelable(false)
        .show {
            title(text = getString(R.string.survey_incomplete_data))
            positiveButton { onClick() }
        }
}
