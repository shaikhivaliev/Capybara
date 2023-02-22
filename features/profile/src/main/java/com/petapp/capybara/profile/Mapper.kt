package com.petapp.capybara.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.model.Chip
import com.petapp.capybara.model.IconTitleDescription

@Composable
fun Profile.toUiData(): IconTitleDescription {
    return IconTitleDescription(
        icon = photo,
        title = name,
        description = stringResource(R.string.surveys_amount, surveys.size.toString())
    )
}
