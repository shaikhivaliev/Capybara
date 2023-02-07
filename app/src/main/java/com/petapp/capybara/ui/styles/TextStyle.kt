package com.petapp.capybara.ui.styles

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.petapp.capybara.R

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

fun textMediumSmall(): TextStyle {
    return defaultTextStyle.copy(
        fontSize = 20.sp
    )
}

fun textMedium(): TextStyle {
    return defaultTextStyle.copy(
        fontSize = 24.sp
    )
}

fun textLarge(): TextStyle {
    return defaultTextStyle.copy(
        fontSize = 32.sp
    )
}






