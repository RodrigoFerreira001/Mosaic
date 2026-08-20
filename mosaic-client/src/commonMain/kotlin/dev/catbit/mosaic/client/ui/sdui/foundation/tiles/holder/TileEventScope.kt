package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

/**
 * Receiver passed to a [TileHolder]'s `onTileEvent`/`onTileGroupEvent` overrides — a narrow slice of
 * [BuilderScope], exposing only the "build a new holder" helpers those two callbacks might need.
 *
 * Most `onTileEvent` overrides never touch this receiver at all (they just do `tile =
 * tile.copy(...)`) — it exists for the rarer case where handling a local event needs to construct a
 * *new* holder on the spot, e.g. a tile whose local state includes nested tile/event holders that
 * get rebuilt in response to the event rather than patched via [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder.update].
 */
class TileEventScope(
    private val builderScope: BuilderScope
) {
    /** Builds a holder for [tileSchema] — delegates to [BuilderScope.buildTileHolder]. */
    fun buildTileHolder(tileSchema: TileSchema): TileHolder<*> =
        builderScope.buildTileHolder(tileSchema)

    /** Builds a holder for [eventSchema] — delegates to [BuilderScope.buildEventHolder]. */
    fun buildEventHolder(eventSchema: EventSchema): EventHolder<*> =
        builderScope.buildEventHolder(eventSchema)

    /** Builds a holder for every entry in this list — delegates to [BuilderScope.buildEventHolders].
     * Unlike the `BuilderScope` extension, the receiver here is non-nullable `List<EventSchema>`. */
    fun List<EventSchema>.buildEventHolders(): MutableList<EventHolder<*>> =
        with(builderScope) { buildEventHolders() }

    /** Builds a holder for every entry in this list — delegates to [BuilderScope.buildTileHolders].
     * Unlike the `BuilderScope` extension, the receiver here is non-nullable `List<TileSchema>`. */
    fun List<TileSchema>.buildTileHolders(): MutableList<TileHolder<*>> =
        with(builderScope) { buildTileHolders() }
}