package dev.catbit.mosaic.core.data.tile

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel

interface TileModel {
    val id: String
    val events: List<EventModel>?
    val style: StyleModel
}