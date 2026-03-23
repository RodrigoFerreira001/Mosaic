package dev.catbit.mosaic.server.builder.tile.builders.navigation

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NestedNavigationGraphTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class NestedNavigationGraphTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val navigatorId: String,
    private val entries: NestedNavigationGraphEntryBuilderScope.() -> Unit,
    private val startEntryId: String
) : TileSchemaBuilder<NestedNavigationGraphTileSchema> {

    override fun build() = NestedNavigationGraphTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilder().apply { StyleSchemaBuilderScope().apply(style) }.build(),
        visibility = visibility,
        navigatorId = navigatorId,
        entries = NestedNavigationGraphEntryBuilderScope().apply(entries).build(),
        startEntryId = startEntryId
    )
}

fun TileSchemaBuilderScope.NestedNavigationGraph(
    id: String = randomUuid(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    navigatorId: String,
    startEntryId: String,
    entries: NestedNavigationGraphEntryBuilderScope.() -> Unit
) {
    addBuilder(
        NestedNavigationGraphTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            navigatorId = navigatorId,
            entries = entries,
            startEntryId = startEntryId
        )
    )
}

class NestedNavigationGraphEntryBuilder(
    private val screenId: String,
    private val initialTiles: TileSchemaBuilderScope.() -> Unit = {},
    private val initialEvents: EventSchemaBuilderScope.() -> Unit = {},
    private val failureTiles: TileSchemaBuilderScope.() -> Unit = {},
    private val failureEvents: EventSchemaBuilderScope.() -> Unit = {},
) : GenericBuilder<NestedNavigationGraphTileSchema.Entry> {

    override fun build() = NestedNavigationGraphTileSchema.Entry(
        screenId = screenId,
        initialTiles = TileSchemaBuilderScope().apply(initialTiles).build(),
        initialEvents = EventSchemaBuilderScope().apply(initialEvents).build(),
        failureTiles = TileSchemaBuilderScope().apply(failureTiles).build(),
        failureEvents = EventSchemaBuilderScope().apply(failureEvents).build()
    )
}

class NestedNavigationGraphEntryBuilderScope :
    GenericBuilderScope<NestedNavigationGraphTileSchema.Entry, NestedNavigationGraphEntryBuilder>() {
    fun addEntry(
        screenId: String,
        initialTiles: TileSchemaBuilderScope.() -> Unit = {},
        initialEvents: EventSchemaBuilderScope.() -> Unit = {},
        failureTiles: TileSchemaBuilderScope.() -> Unit = {},
        failureEvents: EventSchemaBuilderScope.() -> Unit = {},
    ) {
        addBuilder(
            NestedNavigationGraphEntryBuilder(
                screenId = screenId,
                initialTiles = initialTiles,
                initialEvents = initialEvents,
                failureTiles = failureTiles,
                failureEvents = failureEvents
            )
        )
    }
}
