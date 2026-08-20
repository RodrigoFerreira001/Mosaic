package dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Screen-scoped pub/sub channel — the mechanism behind `EventRunningScope.broadcastData(...)` and
 * `TileRenderingScope`'s `observeScreenTileBroadcastChannel`. One fresh instance per screen (a plain
 * field on `MosaicScreenStateHolder`, not a Koin single), reached in composition via
 * `LocalScreenTilesBroadcastChannel` — unlike
 * [dev.catbit.mosaic.client.ui.sdui.foundation.system_broadcast.SystemBroadcastChannel], which is
 * app-wide. This is the channel `ScrollColumnTile`/`ScrollRowTile`/`ScrollPagerTile` and the overlay
 * open/close/dismiss commands (`DisplayNavigationDrawer`, `DisplaySnackbar`, etc.) publish on.
 */
@Immutable
class ScreenTilesBroadcastChannel {

    private val internalChannel = MutableSharedFlow<ScreenTilesBroadcastData>()

    /** Read-only view of the channel — collected via `TileSchema.observeScreenTileBroadcastChannel`
     * (`extensions/SharedFlowExtensions.kt`). */
    val channel get() = internalChannel.asSharedFlow()

    /**
     * Publishes [data] to every current subscriber on this screen. Suspends until every current
     * collector has received it; a broadcast with no active subscribers (e.g. the target tile isn't
     * currently composed) is simply dropped, not queued — matching `ScrollColumnTile`'s own
     * documented behavior of being a no-op when no tile with the target id is mounted.
     *
     * @param data the value to publish — carries its own `tileId` identifying the intended
     * recipient(s).
     */
    suspend fun broadcast(
        data: ScreenTilesBroadcastData
    ) {
        internalChannel.emit(
            data
        )
    }
}