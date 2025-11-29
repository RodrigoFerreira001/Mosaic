package dev.catbit.mosaic.server.builders.tile

import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.server.builders.GenericBuilder

interface TileModelBuilder<out T : TileModel> : GenericBuilder<T>