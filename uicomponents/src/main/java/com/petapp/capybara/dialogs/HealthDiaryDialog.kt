package com.petapp.capybara.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


@Composable
fun HealthDiaryDialog(
    title: Int,
    add: () -> Unit,
    dismiss: () -> Unit
) {
    Dialog(onDismissRequest = dismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column {
                Column(Modifier.padding(24.dp)) {
                    Text(text = stringResource(title))
                    Spacer(Modifier.size(16.dp))
                }
                Spacer(Modifier.size(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        dismiss()
                    }) {
                        Text(text = stringResource(android.R.string.ok))
                    }
                }
            }
        }
    }
}
