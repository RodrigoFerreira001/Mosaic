package dev.catbit.mosaic.core.data.schemas.tile

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

@Immutable
interface TileSchema {
    val id: String
    val events: SerializableImmutableList<EventSchema>?
    val style: StyleSchema
    val visibility: Visibility

    enum class Visibility {
        VISIBLE, INVISIBLE, GONE
    }

    fun isVisible() = visibility == Visibility.VISIBLE
    fun isGone() = visibility == Visibility.GONE
}