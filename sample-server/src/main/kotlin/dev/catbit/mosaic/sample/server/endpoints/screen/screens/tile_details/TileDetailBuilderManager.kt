package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details

import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

class TileDetailBuilderManager(
    private val builders: List<TileDetailBuilder>
) {
    fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        val builder = builders.first { it.canBuild(tileName) }
        with(builder) { buildDetail(tileName) }
    }
}
