package dev.catbit.mosaic.server.builder.tile.builders.system

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.system.SystemBroadcastListenerTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

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

fun TileSchemaBuilderScope.SystemBroadcastListener(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    searchableTerms: List<String>? = null,
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
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
