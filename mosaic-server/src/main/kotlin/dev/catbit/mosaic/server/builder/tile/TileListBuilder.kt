package dev.catbit.mosaic.server.builder.tile

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import dev.catbit.mosaic.server.builder.GenericBuilder

class TileListBuilder(
    private val tiles: TileSchemaBuilderScope.() -> Unit = {},
) : GenericBuilder<SerializableImmutableList<TileSchema>>() {

    override fun build(): SerializableImmutableList<TileSchema> =
        TileSchemaBuilderScope().apply(tiles).build()
}

fun TileList(
    tiles: TileSchemaBuilderScope.() -> Unit = {},
): SerializableImmutableList<TileSchema> = TileListBuilder(
    tiles = tiles
).build()