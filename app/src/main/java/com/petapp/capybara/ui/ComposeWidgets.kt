package com.petapp.capybara.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.petapp.capybara.ui.styles.textSmall

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
fun DeleteButton(title: Int, onClick: () -> Unit) {
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

@Composable
fun OutlinedTextFieldOneLine(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    OutlinedTextField(
        singleLine = true,
        modifier = modifier.apply {
            focusRequester(focusRequester)
        },
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            })
    )
}
