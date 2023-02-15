package com.petapp.capybara.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.petapp.capybara.styles.textMedium
import com.petapp.capybara.uicomponents.R

@Composable
fun ErrorState() {
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
            text = stringResource(R.string.error_explanation),
            style = textMedium(),
            modifier = Modifier.padding(start = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}
