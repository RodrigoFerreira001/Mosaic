package dev.catbit.mosaic.server.builder.tile.builders.system

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.system.SystemBroadcastListenerTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class SystemBroadcastListenerTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val tiles: TileSchemaBuilderScope.() -> Unit
) : TileSchemaBuilder<SystemBroadcastListenerTileSchema>() {

    override fun build() = SystemBroadcastListenerTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        tiles = TileSchemaBuilderScope().apply(tiles).build()
    )
}

/**
 * Renders [tiles] and, while they are on screen, subscribes to the app-wide system broadcast
 * channel — the tile that lets host-app signals (push notifications, connectivity changes,
 * anything the client publishes on that channel) drive server-declared event flows. The
 * subscription lives only as long as the tile is composed; while it is off screen, broadcasts are
 * not observed. Children are hosted in a `Box` carrying [style] and [visibility], so they are
 * stacked rather than emitted into the parent's scope. Dispatches `onSystemBroadcast(broadcastId)`
 * for each broadcast received, carrying the broadcast payload as the event's incoming data, so
 * events can be wired per broadcast.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (e.g. `onSystemBroadcast`).
 * @param style Layout/appearance modifiers applied to the wrapping `Box` (size, padding, background, etc).
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param tiles Child tiles rendered while the broadcast channel is subscribed to.
 */
fun TileSchemaBuilderScope.SystemBroadcastListener(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    searchableTerms: List<String>? = null,
    visibility: TileSchema.Visibility = visible(),
    tiles: TileSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        SystemBroadcastListenerTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            tiles = tiles
        )
    )
}
