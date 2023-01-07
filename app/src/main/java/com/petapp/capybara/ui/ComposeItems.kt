package com.petapp.capybara.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.petapp.capybara.R
import com.petapp.capybara.ui.data.Chip
import com.petapp.capybara.ui.data.IconTitleDescription


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun IconTitleDescItem(
    onItemClick: () -> Unit,
    item: IconTitleDescription
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onItemClick()
        },
        verticalAlignment = Alignment.CenterVertically,
        content = {
            GlideImage(
                model = item.icon,
                contentDescription = null,
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .size(76.dp)
                    .border(
                        color = Color.LightGray,
                        width = 2.dp
                    )
            ) {
                it
                    .error(R.drawable.ic_launcher_foreground)
            }
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = item.title,
                    style = textMedium()
                )
                Text(
                    text = item.description,
                    style = textSmall()
                )
            }
        })
//        photo.transitionName = item.name
}

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
        onClick = { item.click(item.id) }
    ) {
        Text(text = item.text)
    }
}

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
