package dev.catbit.mosaic.core.data.schemas.tile

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema

interface TileSchema {
    val id: String
    val events: List<EventSchema>?
    val style: StyleSchema
    val visibility: Visibility

    enum class Visibility {
        VISIBLE, INVISIBLE, GONE
    }

    fun isVisible() = visibility == Visibility.VISIBLE
    fun isGone() = visibility == Visibility.GONE
}