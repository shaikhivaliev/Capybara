package com.petapp.capybara.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.petapp.capybara.ui.data.Chip

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

@Composable
fun BaseLazyColumn(content: LazyListScope.() -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
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

@Composable
fun DeleteButton(title: Int, onClick: () -> Unit){
    TextButton(
        modifier = Modifier.padding(top = 16.dp),
        onClick = {
            onClick()
        }) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(title),
            color = Color.Red,
            style = textSmall(),
            textAlign = TextAlign.Center
        )
    }
}
