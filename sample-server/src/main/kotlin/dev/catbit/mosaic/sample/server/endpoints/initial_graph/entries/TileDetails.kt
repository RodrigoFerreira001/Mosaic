package dev.catbit.mosaic.sample.server.endpoints.initial_graph.entries

import dev.catbit.mosaic.server.builder.graph.GraphEntryBuilderScope

fun GraphEntryBuilderScope.TileDetails() {
    entry(
        screenId = "tileDetails",
        initialTiles = { LoadingTiles() },
    )
}
