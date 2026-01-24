package dev.catbit.mosaic.client.ui.sdui.foundation.models

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.tile.TileHolder

sealed interface InsertionPosition {
    data object Start : InsertionPosition
    data object End : InsertionPosition
    data class BeforeTile(val tileId: String) : InsertionPosition
    data class AfterTile(val tileId: String) : InsertionPosition
    data class AtIndex(val index: Int) : InsertionPosition

    fun toIndex(target: List<TileHolder<*>>) = when (this) {
        Start -> 0
        End -> target.size
        is AtIndex -> index.coerceIn(0..target.size)
        is BeforeTile -> target.indexOfFirst { it.id == tileId }.takeIf { it != -1 } ?: target.size
        is AfterTile -> target.indexOfFirst { it.id == tileId }.takeIf { it != -1 }?.plus(1) ?: target.size
    }
}