package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 card container that groups its child tiles inside a surface with
 * elevation, shape, and color defined by the Material theme. The visual variant is controlled
 * by [kind]: `DEFAULT` renders a filled card, `ELEVATED` renders a card with a shadow, and
 * `OUTLINED` renders a card with a border.
 *
 * **Updatable fields (via UpdateTiles):** `tiles: SerializableImmutableList<TileSchema>`, `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `kind: Kind`
 *
 * **Triggers dispatched:** `OnClickEventTrigger` — fired every time the card is tapped. The
 * click handler is always wired regardless of whether child events are present.
 *
 * **Notes:** The card uses a [ColumnScope] internally, so it exposes [LocalColumnScope] to
 * children that need column-scoped modifiers (e.g. `weight`). Unlike generic containers, the
 * click handler is always wired to `EventTriggers.onClick()` regardless of whether `events`
 * is null, meaning child events must be registered to observe clicks.
 */
@Immutable
@Serializable
@SerialName("Card")
data class CardTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("kind") val kind: Kind,
) : TileSchema {

    enum class Kind {
        DEFAULT, ELEVATED, OUTLINED
    }
}