package com.petapp.capybara.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.petapp.capybara.R
import com.petapp.capybara.ui.data.IconTitleDescription


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CircleIconTitleDescItem(
    onItemClick: () -> Unit,
    modifier: Modifier,
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
                modifier = modifier
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

@Composable
fun EmptyData(text: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(R.drawable.ic_capybara),
            contentDescription = null,
            contentScale = ContentScale.Inside
        )
        Text(
            text = text,
            style = textMedium(),
            modifier = Modifier.padding(start = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}
