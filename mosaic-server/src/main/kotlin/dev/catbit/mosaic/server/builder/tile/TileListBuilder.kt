package dev.catbit.mosaic.server.builder.tile

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import dev.catbit.mosaic.server.builder.GenericBuilder

/** Compiles a standalone [tiles] block into a plain tile list, independent of any parent tile. */
class TileListBuilder(
    private val tiles: TileSchemaBuilderScope.() -> Unit = {},
) : GenericBuilder<SerializableImmutableList<TileSchema>>() {

    override fun build(): SerializableImmutableList<TileSchema> =
        TileSchemaBuilderScope().apply(tiles).build()
}

/**
 * Builds a standalone, serializable list of tiles from [tiles] — useful for composing a reusable
 * group of tiles as a plain value (e.g. to pass into an event field that expects tile schemas
 * directly, such as `LazyTiles`' response payload) rather than as a nested tile builder lambda.
 *
 * @param tiles Tiles to compile into the list. Defaults to none (empty list).
 */
fun TileList(
    tiles: TileSchemaBuilderScope.() -> Unit = {},
): SerializableImmutableList<TileSchema> = TileListBuilder(
    tiles = tiles
).build()