package dev.catbit.mosaic.core.data.tile

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel

interface TileModel {
    val id: String
    val events: List<EventModel>?
    val style: StyleModel
    val visibility: Visibility

    enum class Visibility {
        VISIBLE, INVISIBLE, GONE
    }

    fun isVisible() = visibility == Visibility.VISIBLE
    fun isGone() = visibility == Visibility.GONE
}