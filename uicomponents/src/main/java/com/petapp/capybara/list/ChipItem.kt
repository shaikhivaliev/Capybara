package com.petapp.capybara.list

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.petapp.capybara.model.COLORS
import com.petapp.capybara.model.Chip
import com.petapp.capybara.styles.mainBlack
import com.petapp.capybara.styles.neutralN40
import com.petapp.capybara.uicomponents.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChipItem(item: Chip) {
    FilterChip(
        selected = item.selected,
        leadingIcon = {
            if (item.selected) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_done),
                    contentDescription = null
                )
            }
        },
        colors = ChipDefaults.filterChipColors(
            backgroundColor = COLORS.find { it.second == item.color }?.first ?: neutralN40,
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
