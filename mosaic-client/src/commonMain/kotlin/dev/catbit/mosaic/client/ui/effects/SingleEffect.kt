package dev.catbit.mosaic.client.ui.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope

@Composable
fun SingleEffect(
    block: suspend CoroutineScope.() -> Unit
) {
    LaunchedEffect(Unit) {
        block()
    }
}