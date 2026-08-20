package dev.catbit.mosaic.client.ui.sdui.foundation.system_broadcast

import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * App-wide pub/sub channel — the mechanism behind `BroadcastToSystem` and
 * `SystemBroadcastListener`'s `OnSystemBroadcast(broadcastId)` trigger. A single Koin `single`
 * instance backs the whole app, so a value published from any screen's event chain reaches every
 * `SystemBroadcastListener` mounted anywhere, on any screen — unlike
 * [dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast.ScreenTilesBroadcastChannel],
 * which is scoped to one screen. Backed by a `MutableSharedFlow` with no replay, so a subscriber only
 * sees values published *after* it started collecting — nothing sent before it existed.
 */
class SystemBroadcastChannel {
    private val internalChannel = MutableSharedFlow<SystemBroadcastData>()

    /** Read-only view of the channel — collected via `observeSystemBroadcastChannel`
     * (`extensions/SharedFlowExtensions.kt`) or directly. */
    val channel get() = internalChannel.asSharedFlow()

    /**
     * Publishes [data] under [broadcastId] to every current subscriber — the mechanism behind
     * `BroadcastToSystem`. Suspends until every current collector has received the value (plain
     * `SharedFlow.emit` semantics); a broadcast with no active subscribers is simply dropped, not
     * queued.
     *
     * @param broadcastId channel id — matched against `OnSystemBroadcast(broadcastId)` on the
     * receiving end.
     * @param data the payload to publish.
     */
    suspend fun broadcast(
        broadcastId: String,
        data: AnySerializable
    ) {
        internalChannel.emit(
            SystemBroadcastData(
                broadcastId = broadcastId,
                data = data
            )
        )
    }
}