package dev.catbit.mosaic.server.builder.tile

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.server.builder.GenericBuilder

/** Base class every tile builder extends; produces the concrete [TileSchema] instance [T] the tile compiles down to. */
abstract class TileSchemaBuilder<out T : TileSchema> : GenericBuilder<T>()

/** Tile is shown and takes up space in the layout. The default visibility for every tile. */
fun visible() = TileSchema.Visibility.VISIBLE

/** Tile is hidden but still reserves its layout space, as if it were transparent. */
fun invisible() = TileSchema.Visibility.INVISIBLE

/** Tile is removed from the layout entirely — it renders nothing and takes up no space. */
fun gone() = TileSchema.Visibility.GONE
