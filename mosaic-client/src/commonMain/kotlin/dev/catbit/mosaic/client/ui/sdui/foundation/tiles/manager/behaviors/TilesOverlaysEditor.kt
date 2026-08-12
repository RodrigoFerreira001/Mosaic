package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

interface TilesOverlaysEditor {

    fun addBottomSheet(
        id: String,
        isCancellable: Boolean,
        fill: Boolean,
        allowsPartialExpansion: Boolean,
        tileSchemas: List<TileSchema>
    ): Result<Unit>

    fun dismissBottomSheet(
        id: String
    ): Result<Unit>

    fun addModalBottomSheet(
        id: String,
        isCancellable: Boolean,
        fill: Boolean,
        allowsPartialExpansion: Boolean,
        tileSchemas: List<TileSchema>
    ): Result<Unit>

    fun dismissModalBottomSheet(
        id: String
    ): Result<Unit>

    fun addDialog(
        id: String,
        isCancellable: Boolean,
        usePlatformDefaultWidth: Boolean,
        tileSchemas: List<TileSchema>
    ): Result<Unit>

    fun dismissDialog(
        id: String
    ): Result<Unit>
}