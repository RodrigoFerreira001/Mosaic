package dev.catbit.mosaic.server.builder.tile

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.server.builder.GenericBuilder

class TileListBuilder(
    private val tiles: TileSchemaBuilderScope.() -> Unit = {},
) : GenericBuilder<List<TileSchema>> {

    override fun build() = TileSchemaBuilderScope().apply(tiles).build()
}

fun TileList(
    tiles: TileSchemaBuilderScope.() -> Unit = {},
) = TileListBuilder(
    tiles = tiles
).build()