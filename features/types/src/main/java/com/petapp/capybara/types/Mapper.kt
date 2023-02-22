package com.petapp.capybara.types

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.petapp.capybara.core.data.model.Type
import com.petapp.capybara.model.IconTitleDescription

@Composable
fun Type.toUiData(): IconTitleDescription {
    return IconTitleDescription(
        icon = icon,
        title = name,
        description = stringResource(R.string.surveys_amount, surveys.size.toString())
    )
}
