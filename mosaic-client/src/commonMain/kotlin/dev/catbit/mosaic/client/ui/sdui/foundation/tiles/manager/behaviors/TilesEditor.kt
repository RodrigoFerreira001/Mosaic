package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors

import dev.catbit.mosaic.client.ui.sdui.foundation.models.InsertionPosition
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

interface TilesEditor {

    fun addTile(
        tileSchema: TileSchema,
        where: InsertionPosition = InsertionPosition.End,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    )

    fun addTiles(
        tileSchemas: List<TileSchema>,
        where: InsertionPosition = InsertionPosition.End,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    )

    fun addTile(
        tileSchema: TileSchema,
        groupingTileId: String,
        where: InsertionPosition = InsertionPosition.End,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    )

    fun addTiles(
        tileSchemas: List<TileSchema>,
        groupingTileId: String,
        where: InsertionPosition = InsertionPosition.End,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    )

    fun removeTile(
        tileId: String,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    )

    fun removeTile(
        tileId: String,
        groupingTileId: String,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    )

    fun removeTiles(
        tileIds: List<String>,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    )

    fun removeTiles(
        tileIds: List<String>,
        groupingTileId: String,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    )

    fun replaceTiles(
        tileSchemas: List<TileSchema>,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    )

    fun replaceTiles(
        tileSchemas: List<TileSchema>,
        groupingTileId: String,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    )

    fun wipeTiles(
        groupingTileId: String,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    )

    fun updateTile(
        tileId: String,
        updateData: Map<String, Any?>,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    )
}