package com.petapp.capybara.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.petapp.capybara.ui.model.Chip

@Composable
fun ChipLazyRow(chips: List<Chip>) {
    LazyRow(
        modifier = Modifier.padding(top = 12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            items(chips) { item ->
                ChipItem(item)
            }
        })
}
