package com.petapp.capybara.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.petapp.capybara.ui.styles.textSmall

@Composable
fun IconTitleItem(icon: Int, title: Int, onClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)
        .clickable {
            onClick()
        }) {
        Row(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 8.dp)) {
            Image(
                painter = painterResource(icon),
                contentDescription = null
            )
            Text(
                text = stringResource(title),
                style = textSmall(),
                modifier = Modifier.padding(start = 24.dp)
            )
        }
    }
}
