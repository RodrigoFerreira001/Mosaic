package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors

import dev.catbit.mosaic.client.ui.sdui.foundation.models.InsertionPosition
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

interface TilesEditor {

    fun addTile(
        tileSchema: TileSchema,
        where: InsertionPosition = InsertionPosition.End,
    ): Result<Unit>

    fun addTiles(
        tileSchemas: List<TileSchema>,
        where: InsertionPosition = InsertionPosition.End,
    ): Result<Unit>

    fun addTile(
        tileSchema: TileSchema,
        groupingTileId: String,
        where: InsertionPosition = InsertionPosition.End,
    ): Result<Unit>

    fun addTiles(
        tileSchemas: List<TileSchema>,
        groupingTileId: String,
        where: InsertionPosition = InsertionPosition.End,
    ): Result<Unit>

    fun removeTile(
        tileId: String,
    ): Result<Unit>

    fun removeTile(
        tileId: String,
        groupingTileId: String,
    ): Result<Unit>

    fun removeTiles(
        tileIds: List<String>,
    ): Result<Unit>

    fun removeTiles(
        tileIds: List<String>,
        groupingTileId: String,
    ): Result<Unit>

    fun replaceTiles(
        tileSchemas: List<TileSchema>,
    ): Result<Unit>

    fun replaceTiles(
        tileSchemas: List<TileSchema>,
        groupingTileId: String,
    ): Result<Unit>

    fun wipeTiles(
        groupingTileId: String,
    ): Result<Unit>

    fun updateTile(
        tileId: String,
        updateData: Map<String, Any?>,
    ): Result<Unit>

    fun checkIfTileHasChildren(
        groupingTileId: String,
        childrenIds: List<String>
    ): Boolean

    fun getTileChildrenCount(
        groupingTileId: String
    ): Int?
}