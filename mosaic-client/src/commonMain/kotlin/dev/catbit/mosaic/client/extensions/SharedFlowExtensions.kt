package dev.catbit.mosaic.client.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.catbit.mosaic.client.ui.effects.SingleEffect
import dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast.ScreenTilesBroadcastData
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalScreenTilesBroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.system_broadcast.SystemBroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.system_broadcast.SystemBroadcastData
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

/**
 * Collects this [SharedFlow] inside a [LaunchedEffect] keyed on [key], running [action] for each
 * emitted value via `collectLatest` — a still-running [action] from a previous emission is cancelled
 * when a new one arrives, rather than queued. The shared low-level primitive behind
 * [observeScreenTileBroadcastChannel] and [observeSystemBroadcastChannel] below.
 *
 * @param key restarts the underlying collection (and cancels any in-flight [action]) whenever it
 * changes, the same as any `LaunchedEffect` key.
 * @param action run for each collected value.
 */
@Composable
fun <T> SharedFlow<T>.consume(
    key: Any?,
    action: suspend (value: T) -> Unit
) {
    LaunchedEffect(key) {
        collectLatest {
            action(it)
        }
    }
}

/**
 * Subscribes this tile to its screen's own broadcast channel (reached via
 * [LocalScreenTilesBroadcastChannel]), running [action] for every emitted [ScreenTilesBroadcastData]
 * of type [T] — the composable-side counterpart of `EventRunningScope.broadcastData`, and the
 * mechanism a tile's own renderer uses to react to a screen-scoped command addressed to it (scroll
 * commands, overlay open/close, etc.).
 *
 * @param filterByTileId when `true` (the default), only values whose `tileId` equals this tile's own
 * `id` reach [action] — the usual case, since most broadcasts are addressed to one specific tile.
 * Pass `false` for a tile that should react to every [T] on the channel regardless of which tile it
 * was addressed to (e.g. `SystemBroadcastListener`, which listens for events by `broadcastId`, not by
 * `tileId`).
 * @param action run for each matching broadcast value.
 */
@Composable
inline fun <reified T : ScreenTilesBroadcastData> TileSchema.observeScreenTileBroadcastChannel(
    filterByTileId: Boolean = true,
    crossinline action: suspend (value: T) -> Unit
) {
    LocalScreenTilesBroadcastChannel.current.channel.consume(id) { data ->
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

/**
 * Subscribes to the app-wide [SystemBroadcastChannel] (resolved via Koin), running [action] for
 * every published [SystemBroadcastData] — the composable-side counterpart of `BroadcastToSystem`, and
 * the mechanism behind `SystemBroadcastListener`'s `OnSystemBroadcast(broadcastId)` trigger, reaching
 * across screens rather than being scoped to one.
 *
 * @param key restarts the underlying subscription whenever it changes, the same as any
 * `LaunchedEffect` key.
 * @param action run for each published value.
 */
@Composable
fun observeSystemBroadcastChannel(
    key: Any?,
    action: suspend (value: SystemBroadcastData) -> Unit
) {
    koinInject<SystemBroadcastChannel>().channel.consume(key) { data ->
        action(data)
    }
}