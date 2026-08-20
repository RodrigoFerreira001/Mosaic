package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileGroupEvent
import dev.catbit.mosaic.core.data.schemas.event.EventSchema

/**
 * Surface a screen's `TilesManager` exposes for routing locally-raised [TileEvent]/[TileGroupEvent]
 * instances to the tile(s) that should handle them, and for looking up/patching [EventSchema]s
 * already registered anywhere in the screen's tile tree by id — the collaborator behind
 * [dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope.tilesEventDispatcher] and
 * [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope]'s own
 * `dispatchEvent`/`dispatchGroupEvent` calls (via the `UIEvent` those emit).
 */
interface TilesEventDispatcher {
    /**
     * Delivers [event] to the `TileHolder` whose id equals [tileId], via that holder's own
     * `onTileEvent` override — the routing behind
     * [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope.dispatchEvent].
     *
     * @param tileId id of the target tile.
     * @param event the local event to apply.
     */
    fun onEvent(
        tileId: String,
        event: TileEvent,
    ): Result<Unit>

    /**
     * Delivers [event] to every `TileHolder` in the whole screen's tree whose `handlesGroupEvent`
     * returns `true` for it, via each one's own `onTileGroupEvent` override — the routing behind
     * [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope.dispatchGroupEvent].
     *
     * @param event the group event to broadcast.
     */
    fun onGroupEvent(
        event: TileGroupEvent,
    ): Result<Unit>

    /**
     * Looks up the [EventSchema] registered under [eventId] anywhere in the screen's tile tree — the
     * lookup behind `TriggerEvent`.
     *
     * @param eventId id of the event to look up.
     * @return the matching [EventSchema], or `null` if no event carries that id on this screen.
     */
    fun getEventSchema(eventId: String): EventSchema?

    /**
     * Applies [data] as a shallow JSON-patch merge onto the event registered under [eventId] —
     * the mechanism behind `UpdateEvents`, the event-level equivalent of `TilesEditor.updateTile`.
     *
     * @param eventId id of the event to patch.
     * @param data the patch to merge onto that event's current fields.
     */
    fun updateEventHolder(
        eventId: String,
        data: Map<String, Any?>
    ): Result<Unit>
}