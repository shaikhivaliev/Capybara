package com.petapp.capybara.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.petapp.capybara.R
import com.petapp.capybara.ui.model.Chip
import com.petapp.capybara.ui.styles.mainBlack

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChipItem(item: Chip) {
    FilterChip(
        selected = item.selected,
        leadingIcon = {
            if (item.selected) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_done_black),
                    contentDescription = null
                )
            }
        },
        colors = ChipDefaults.filterChipColors(
            backgroundColor = Color(item.color),
            contentColor = mainBlack
        ),
        onClick = {
            item.click(item.id)
        }
    ) {
        Text(
            text = item.text
        )
    }
}
