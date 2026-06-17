package dev.catbit.mosaic.client.extensions

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.effects.SingleEffect
import dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast.ScreenTilesBroadcastData
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalScreenTilesBroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.system_broadcast.SystemBroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.system_broadcast.SystemBroadcastData
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

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
inline fun <reified T : ScreenTilesBroadcastData> TileSchema.observeScreenTileBroadcastChannel(
    filterByTileId: Boolean = true,
    crossinline action: suspend (value: T) -> Unit
) {
    LocalScreenTilesBroadcastChannel.current.channel.consume { data ->
        if (data is T) {
            if (filterByTileId) {
                if (data.tileId == id) {
                    action(data)
                }
            } else {
                action(data)
            }
        }
    }
}

@Composable
fun observeSystemBroadcastChannel(
    action: suspend (value: SystemBroadcastData) -> Unit
) {
    koinInject<SystemBroadcastChannel>().channel.consume { data ->
        action(data)
    }
}