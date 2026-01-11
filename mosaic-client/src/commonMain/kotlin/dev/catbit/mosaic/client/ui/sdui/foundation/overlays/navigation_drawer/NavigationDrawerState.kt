package dev.catbit.mosaic.client.ui.sdui.foundation.overlays.navigation_drawer

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NavigationDrawerState(
    private val coroutineScope: CoroutineScope,
    internal val drawerState: DrawerState
) {

    var content by mutableStateOf<@Composable () -> Unit>({})
        private set

    var onDismissCallback: (() -> Unit)? = null
        private set

    fun updateContent(
        content: @Composable () -> Unit
    ) {
        this.content = content
    }

    fun dismiss() {
        // TODO try-catch
        coroutineScope.launch {
            drawerState.close()
            onDismissCallback?.invoke()
        }
    }

    fun show(
        onDismiss: (() -> Unit)? = null,
    ) {
        onDismissCallback = onDismiss
        this.content = content
        coroutineScope.launch {
            drawerState.open()
        }
    }
}

@Composable
fun rememberNavigationDrawerState(
    drawerState: DrawerState,
    coroutineScope: CoroutineScope
) = remember {
    NavigationDrawerState(
        drawerState = drawerState,
        coroutineScope = coroutineScope
    )
}