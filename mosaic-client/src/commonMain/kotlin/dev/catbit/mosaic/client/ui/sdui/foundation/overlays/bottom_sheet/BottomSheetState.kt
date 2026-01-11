package dev.catbit.mosaic.client.ui.sdui.foundation.overlays.bottom_sheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetState(
    private val coroutineScope: CoroutineScope,
    internal val modalBottomSheetState: SheetState,
) {
    var isVisible by mutableStateOf(false)
        private set

    var isCancellable by mutableStateOf(true)

    var content by mutableStateOf<@Composable ColumnScope.() -> Unit>({})
        private set

    private var onDismissCallback: (() -> Unit)? = null

    fun onDismissRequest() {
        onDismissCallback?.invoke()
        isVisible = false
    }

    fun dismiss() {
        // TODO try-catch
        coroutineScope.launch {
            modalBottomSheetState.hide()
        }.invokeOnCompletion {
            onDismissCallback?.invoke()
            isVisible = false
        }
    }

    fun updateContent(
        content: @Composable ColumnScope.() -> Unit
    ) {
        this.content = content
    }

    fun show(
        fill: Boolean = false,
        cancellable: Boolean = true,
        onDismiss: (() -> Unit)? = null,
    ) {
        onDismissCallback = onDismiss
        isCancellable = cancellable
        isVisible = true

        coroutineScope.launch {
            if (fill) {
                modalBottomSheetState.expand()
            } else {
                modalBottomSheetState.show()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun rememberBottomSheetState(
    modalBottomSheetState: SheetState,
    coroutineScope: CoroutineScope,
) = remember {
    BottomSheetState(
        modalBottomSheetState = modalBottomSheetState,
        coroutineScope = coroutineScope,
    )
}