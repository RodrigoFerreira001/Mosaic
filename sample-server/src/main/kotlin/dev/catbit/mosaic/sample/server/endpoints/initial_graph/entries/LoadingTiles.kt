package dev.catbit.mosaic.sample.server.endpoints.initial_graph.entries

import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLowest
import dev.catbit.mosaic.server.builder.placement.alignToCenter
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.progress.CircularProgressIndicator

/**
 * Shared `initialTiles` for every root-graph entry — an indeterminate spinner centered on the
 * screen background, shown while this entry's `GetScreen` (the default `initialEvents`) is in
 * flight. Without this, entries default to a blank screen for that window.
 */
fun TileSchemaBuilderScope.LoadingTiles() {
    Box(
        style = {
            size(width = fillHorizontally(), height = fillVertically())
            windowInsets(windowInsetsSystemBars())
            background(color(themeColorSurfaceContainerLowest()))
        },
        alignment = alignToCenter()
    ) {
        CircularProgressIndicator(
            style = { size(width = fixedHorizontally(40), height = fixedVertically(40)) }
        )
    }
}
