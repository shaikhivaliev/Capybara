package com.petapp.capybara.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.petapp.capybara.R
import com.petapp.capybara.ui.data.Chip
import com.petapp.capybara.ui.data.IconTitleDescription


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CircleIconTitleDescItem(
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
                modifier = modifierCircleIcon76dp()
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

@Composable
fun ChipRow(chips: List<Chip>) {
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
fun StandardColumn(content: LazyListScope.() -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}

@Composable
fun ShowSnackbar(
    scaffoldState: ScaffoldState,
    errorMessage: String,
    action: (() -> Unit)? = null,
    dismissed: (() -> Unit)? = null,
    actionLabel: String? = null
) {
    LaunchedEffect(scaffoldState.snackbarHostState) {
        val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
            message = errorMessage,
            actionLabel = actionLabel.orEmpty()
        )
        when (snackbarResult) {
            SnackbarResult.Dismissed -> dismissed?.invoke()
            SnackbarResult.ActionPerformed -> action?.invoke()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandedDropdownMenu(
    label: String,
    selectedTitle: String,
    expanded: Boolean,
    selectionOptions: List<String> = emptyList(),
    onExpandedChange: (Boolean) -> Unit,
    onSelectedText: (String) -> Unit
) {
    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        expanded = expanded,
        onExpandedChange = {
            onExpandedChange(it)
        },
        content = {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                readOnly = true,
                value = selectedTitle,
                onValueChange = {},
                label = { Text(label) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    onExpandedChange(false)
                }
            ) {
                selectionOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            onSelectedText(selectionOption)
                            onExpandedChange(false)
                        }
                    ) {
                        Text(text = selectionOption)
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandedDropdownMenuReadOnly(
    value: String,
    label: String
) {
    OutlinedTextField(
        modifier = Modifier.padding(top = 16.dp),
        enabled = false,
        value = value,
        onValueChange = {},
        label = { Text(label) },
        trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(
                expanded = false
            )
        }
    )
}

@Composable
fun OutlinedTextFieldReadOnly(
    value: String,
    label: String
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        value = value,
        enabled = false,
        onValueChange = {},
        label = { Text(label) }
    )
}
