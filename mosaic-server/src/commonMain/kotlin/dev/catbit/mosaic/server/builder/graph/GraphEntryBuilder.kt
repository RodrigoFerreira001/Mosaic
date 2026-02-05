package dev.catbit.mosaic.server.builder.graph

import dev.catbit.mosaic.core.data.responses.graph.GraphResponse.Entry
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

class GraphEntryBuilder(
    private val screenId: String,
    private val initialTiles: TileSchemaBuilderScope.() -> Unit = {},
    private val initialEvents: EventSchemaBuilderScope.() -> Unit = {},
    private val failureTiles: TileSchemaBuilderScope.() -> Unit = {},
    private val failureEvents: EventSchemaBuilderScope.() -> Unit = {},
) : GenericBuilder<Entry> {

    override fun build() = Entry(
        screenId = screenId,
        initialTiles = TileSchemaBuilderScope().apply(initialTiles).build(),
        initialEvents = EventSchemaBuilderScope().apply(initialEvents).build(),
        failureTiles = TileSchemaBuilderScope().apply(failureTiles).build(),
        failureEvents = EventSchemaBuilderScope().apply(failureEvents).build()
    )
}

class GraphEntryBuilderScope : GenericBuilderScope<Entry, GraphEntryBuilder>()

fun GraphEntryBuilderScope.Entry(
    screenId: String,
    initialTiles: TileSchemaBuilderScope.() -> Unit = {},
    initialEvents: EventSchemaBuilderScope.() -> Unit = {},
    failureTiles: TileSchemaBuilderScope.() -> Unit = {},
    failureEvents: EventSchemaBuilderScope.() -> Unit = {},
) {
    addBuilder(
        GraphEntryBuilder(
            screenId = screenId,
            initialTiles = initialTiles,
            initialEvents = initialEvents,
            failureTiles = failureTiles,
            failureEvents = failureEvents
        )
    )
}