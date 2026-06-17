package dev.catbit.mosaic.server.builder.tile

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.server.builder.GenericBuilder

abstract class TileSchemaBuilder<out T : TileSchema> : GenericBuilder<T>()

fun visible() = TileSchema.Visibility.VISIBLE
fun invisible() = TileSchema.Visibility.INVISIBLE
fun gone() = TileSchema.Visibility.GONE
