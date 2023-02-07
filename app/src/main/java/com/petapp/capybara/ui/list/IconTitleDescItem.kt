package com.petapp.capybara.ui.list

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.petapp.capybara.R
import com.petapp.capybara.ui.model.IconTitleDescription
import com.petapp.capybara.ui.styles.neutralN40
import com.petapp.capybara.ui.styles.textMedium
import com.petapp.capybara.ui.styles.textSmall

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun IconTitleDescItem(
    item: IconTitleDescription,
    contentScale: ContentScale,
    onClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onClick()
        },
        content = {
            GlideImage(
                model = item.icon,
                contentDescription = null,
                contentScale = contentScale,
                modifier = Modifier
                    .size(76.dp)
                    .border(
                        color = neutralN40,
                        width = 1.dp
                    )
            ) {
                it
                    .error(R.drawable.ic_launcher_foreground)
            }
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    text = item.title,
                    style = textMedium()
                )
                Text(
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    text = item.description,
                    style = textSmall()
                )
            }
        })
}
