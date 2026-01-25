package dev.catbit.mosaic.server.builders.tile

import dev.catbit.mosaic.core.data.tile.tiles.buttons.ButtonTileModel
import dev.catbit.mosaic.core.data.tile.tiles.containers.RowTileModel
import dev.catbit.mosaic.server.annotations.BuildFor

@BuildFor(
    [
        ButtonTileModel::class,
        RowTileModel::class
    ]
)
internal object TileModelBuilderBridge