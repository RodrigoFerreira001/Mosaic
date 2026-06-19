package dev.catbit.mosaic.core.data.schemas.tile

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Base contract for all tile schemas. Every tile that participates in the SDUI rendering pipeline
 * must implement this interface.
 *
 * **Base fields shared by all tiles:**
 * - [id] — unique identifier within the screen, used for keying and `UpdateTiles` targeting.
 * - [events] — event list attached to this tile; may be `null` when no events are wired.
 * - [style] — controls size, margin, padding, background, border, clip, and window insets.
 * - [visibility] — `VISIBLE`, `INVISIBLE` (occupies space but not drawn), or `GONE` (removed from layout).
 * - [searchableTerms] — optional list of strings used by parent container tiles (e.g. `ColumnTileSchema`,
 *   `LazyColumnTileSchema`) to filter this tile when their `filterChildrenByTerm` is set. When `null`,
 *   the tile is always shown regardless of the active filter term.
 */
@Immutable
interface TileSchema {
    val id: String
    val events: SerializableImmutableList<EventSchema>?
    val style: StyleSchema
    val visibility: Visibility
    val searchableTerms: SerializableImmutableList<String>?

    enum class Visibility {
        VISIBLE, INVISIBLE, GONE
    }

    fun isVisible() = visibility == Visibility.VISIBLE
    fun isGone() = visibility == Visibility.GONE
}