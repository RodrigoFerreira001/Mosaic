package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details

import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

/**
 * One implementation per tile name, rendering the detail content shown below the
 * "tileDetails" app bar (see [dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailsScreenBuilder]).
 * [TileDetailBuilderManager] picks the first builder whose [canBuild] matches.
 */
interface TileDetailBuilder {
    fun canBuild(tileName: String): Boolean
    fun TileSchemaBuilderScope.buildDetail(tileName: String)
}
