package dev.catbit.mosaic.client.ui.sdui.foundation.state.manager

import dev.catbit.mosaic.client.ui.sdui.foundation.models.InsertionPosition
import dev.catbit.mosaic.core.data.tile.TileModel

interface TilesEditor {

    fun addTile(
        tileModel: TileModel,
        where: InsertionPosition = InsertionPosition.End
    )

    fun addTiles(
        tileModels: List<TileModel>,
        where: InsertionPosition = InsertionPosition.End
    )

    fun addTile(
        tileModel: TileModel,
        groupingTileId: String,
        where: InsertionPosition = InsertionPosition.End
    )

    fun addTiles(
        tileModels: List<TileModel>,
        groupingTileId: String,
        where: InsertionPosition = InsertionPosition.End
    )

    fun updateTile(
        id: String,
        updateData: Map<String, Any?>
    )

    fun removeTile(
        id: String
    )

    fun removeTiles(
        ids: List<String>
    )

    fun wipeTiles()

    fun wipeTiles(
        groupingTileId: String
    )
}