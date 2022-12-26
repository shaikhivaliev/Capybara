package com.petapp.capybara.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@SuppressLint("ModifierFactoryExtensionFunction")
fun modifierCircleIcon76dp(): Modifier {
    return Modifier
        .size(76.dp)
        .clip(CircleShape)
        .border(
            color = Color.LightGray,
            width = 2.dp,
            shape = RoundedCornerShape(percent = 50)
        )
}

@SuppressLint("ModifierFactoryExtensionFunction")
fun modifierBaseList(): Modifier {
    return Modifier
        .padding(start = 16.dp)
        .fillMaxWidth()
}
