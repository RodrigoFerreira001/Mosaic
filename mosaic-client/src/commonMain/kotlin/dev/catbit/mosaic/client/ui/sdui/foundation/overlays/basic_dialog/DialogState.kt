package dev.catbit.mosaic.client.ui.sdui.foundation.overlays.basic_dialog

import androidx.compose.runtime.*

@Stable
class DialogState(initialState: Boolean) {

    var isVisible by mutableStateOf(initialState)
        private set

    var usePlatformDefaultWidth: Boolean = true
        private set

    var isCancellable by mutableStateOf(true)
        private set

    var content by mutableStateOf<@Composable () -> Unit>({})
        private set

    var onDismissCallback: (() -> Unit)? = null
        private set

    fun dismiss() {
        // TODO try-catch
        isVisible = false
        onDismissCallback?.invoke()
    }

    fun updateContent(
        content: @Composable () -> Unit
    ) {
        this.content = content
    }

    fun show(
        cancellable: Boolean = true,
        onDismiss: (() -> Unit)? = null,
        constrainInPlatformWidth: Boolean = true,
    ) {
        isCancellable = cancellable
        onDismissCallback = onDismiss
        usePlatformDefaultWidth = constrainInPlatformWidth
        isVisible = true
    }
}

@Stable
@Composable
fun rememberDialogState() = remember { DialogState(false) }