package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

interface TilesOverlaysEditor {
    fun setBottomSheetTiles(tileSchemas: List<TileSchema>)
    fun setDialogTiles(tileSchemas: List<TileSchema>)
}