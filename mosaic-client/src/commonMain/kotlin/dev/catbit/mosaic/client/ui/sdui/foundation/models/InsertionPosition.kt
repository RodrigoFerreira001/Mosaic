package dev.catbit.mosaic.client.ui.sdui.foundation.models

sealed interface InsertionPosition {
    data object Start : InsertionPosition
    data object End : InsertionPosition
    data class AtIndex(val index: Int) : InsertionPosition

    fun toIndex(target: List<*>) = when (this) {
        Start -> 0
        End -> target.size
        is AtIndex -> index.coerceIn(0..target.size)
    }
}