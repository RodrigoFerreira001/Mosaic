package dev.catbit.mosaic.server.builder.tile

import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.server.builder.GenericBuilder

interface TileModelBuilder<out T : TileModel> : GenericBuilder<T>