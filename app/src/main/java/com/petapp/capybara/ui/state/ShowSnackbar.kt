package com.petapp.capybara.ui.state

import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState

@Composable
fun ShowSnackbar(
    errorMessage: String,
    retryMessage: String? = null,
    snackbarHostState: SnackbarHostState,
    action: (() -> Unit)? = null,
    dismissed: (() -> Unit)? = null
) {
    val onActionState = rememberUpdatedState(action)
    val onDismissedState = rememberUpdatedState(dismissed)

    LaunchedEffect(errorMessage, retryMessage, snackbarHostState) {
        val snackbarResult = snackbarHostState.showSnackbar(
            message = errorMessage,
            actionLabel = retryMessage
        )
        if (snackbarResult == SnackbarResult.ActionPerformed) {
            onActionState.value?.invoke()
        }
        onDismissedState.value?.invoke()
    }
}
