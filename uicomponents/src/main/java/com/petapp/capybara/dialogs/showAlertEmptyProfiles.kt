package com.petapp.capybara.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.petapp.capybara.uicomponents.R

@Composable
fun ShowAlertEmptyProfiles(
    onClick: () -> Unit,
    isOpen: Boolean
) {
    val isDialogOpen = remember { mutableStateOf(isOpen) }
    if (isDialogOpen.value) {
        AlertDialog(
            onDismissRequest = {
                isDialogOpen.value = false
            },
            modifier = Modifier // Set the width and padding
                .fillMaxWidth()
                .padding(32.dp),
            shape = RoundedCornerShape(5.dp),
            backgroundColor = Color.White,
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            title = {
                Text(text = stringResource(R.string.survey_incomplete_data))
            },
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        isDialogOpen.value = false
                        onClick()
                    }) {
                        Text(text = stringResource(android.R.string.ok))
                    }
                }
            }
        )
    }
}
