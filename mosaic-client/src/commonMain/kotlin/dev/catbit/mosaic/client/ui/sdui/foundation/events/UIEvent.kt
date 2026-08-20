package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.core.data.schemas.event.EventSchema

/**
 * The 3 commands a [TileRenderingScope][dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope]
 * emits through its `onEvent` sink — the wire between a tile's own Compose interaction handling and
 * the screen's state holder, which observes these and mutates the live tile tree accordingly. Every
 * `TileRenderingScope` method (`triggerEvent`, `dispatchEvent`, `dispatchGroupEvent`) produces exactly
 * one of these.
 */
sealed interface UIEvent {
    /** Emitted by `TileRenderingScope.triggerEvent` — run every entry in [events] (already filtered
     * to the ones whose `trigger` matched), each with [data] as its `incomingData`. */
    data class EventSchemaHolderUIEvent(
        val events: List<EventSchema>,
        val data: Any?
    ) : UIEvent

    /** Emitted by `TileRenderingScope.dispatchEvent` — deliver [event] to the `TileHolder` whose id
     * equals [tileId], via that holder's own `onTileEvent`. */
    data class TileEventHolderUIEvent(
        val tileId: String,
        val event: TileEvent
    ) : UIEvent

    /** Emitted by `TileRenderingScope.dispatchGroupEvent` — deliver [event] to every `TileHolder` in
     * the whole tree whose `handlesGroupEvent` returns `true` for it. */
    data class TileGroupEventHolderUIEvent(
        val event: TileGroupEvent
    ) : UIEvent
}