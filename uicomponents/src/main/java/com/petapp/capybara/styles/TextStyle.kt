package com.petapp.capybara.styles

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.petapp.capybara.uicomponents.R

private val robotoRegular = FontFamily(Font(R.font.roboto_regular))

private val defaultTextStyle = TextStyle(
    color = mainBlack,
    fontFamily = robotoRegular
)

fun textSmall(): TextStyle {
    return defaultTextStyle.copy(
        fontSize = 16.sp
    )
}

fun textMedium(): TextStyle {
    return defaultTextStyle.copy(
        fontSize = 24.sp
    )
}