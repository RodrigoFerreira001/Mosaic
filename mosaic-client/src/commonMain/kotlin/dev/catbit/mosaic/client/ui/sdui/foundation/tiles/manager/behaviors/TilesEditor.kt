package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors

import dev.catbit.mosaic.client.ui.sdui.foundation.models.InsertionPosition
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

interface TilesEditor {

    fun addTile(
        tileSchema: TileSchema,
        where: InsertionPosition = InsertionPosition.End
    )

    fun addTiles(
        tileSchemas: List<TileSchema>,
        where: InsertionPosition = InsertionPosition.End
    )

    fun addTile(
        tileSchema: TileSchema,
        groupingTileId: String,
        where: InsertionPosition = InsertionPosition.End
    )

    fun addTiles(
        tileSchemas: List<TileSchema>,
        groupingTileId: String,
        where: InsertionPosition = InsertionPosition.End
    )

    fun removeTile(
        tileId: String
    )

    fun removeTile(
        tileId: String,
        groupingTileId: String
    )

    fun removeTiles(
        tileIds: List<String>
    )

    fun removeTiles(
        tileIds: List<String>,
        groupingTileId: String
    )

    fun replaceTiles(
        tileSchemas: List<TileSchema>,
    )

    fun replaceTiles(
        tileSchemas: List<TileSchema>,
        groupingTileId: String
    )

    fun wipeTiles(
        groupingTileId: String
    )

    fun updateTile(
        tileId: String,
        updateData: Map<String, Any?>
    )
}