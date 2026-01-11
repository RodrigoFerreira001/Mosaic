package dev.catbit.mosaic.client.ui.sdui.foundation.overlays.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SnackBarState(
    val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) {

    fun dismiss() {
        // TODO try-catch
        snackbarHostState.currentSnackbarData?.dismiss()
    }

    fun show(
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        onDismiss: (() -> Unit)? = null
    ) {
        coroutineScope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = duration
            )

            // TODO try-catch
            when (result) {
                SnackbarResult.Dismissed -> onDismiss?.invoke()
                SnackbarResult.ActionPerformed -> onAction?.invoke()
            }
        }
    }
}

@Composable
fun rememberSnackBarState(
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) = remember { SnackBarState(snackbarHostState, coroutineScope) }