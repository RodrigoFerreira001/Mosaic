package dev.catbit.mosaic.client.extensions

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.effects.SingleEffect
import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalBroadcastChannel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun <T> SharedFlow<T>.consume(
    action: suspend (value: T) -> Unit
) {
    SingleEffect {
        collectLatest {
            action(it)
        }
    }
}

@Composable
inline fun <reified T: BroadcastData> observeBroadcastChannel(
    crossinline action: suspend (value: T) -> Unit
) {
    LocalBroadcastChannel.current.consume { data ->
        if (data is T) { action(data) }
    }
}