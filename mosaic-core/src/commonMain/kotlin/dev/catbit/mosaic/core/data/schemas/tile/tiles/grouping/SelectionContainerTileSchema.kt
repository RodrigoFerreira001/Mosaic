package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Wraps [tiles] in a Compose `SelectionContainer`, making the text rendered by its descendants
 * selectable and copyable with the platform's selection handles and context menu.
 *
 * **Triggers dispatched:** none. This tile emits no trigger of its own — not even
 * `OnDisplayEventTrigger` — so any `events` declared on it are never fired; wire events on the
 * children instead.
 *
 * **Notes:** children are rendered without a scope CompositionLocal, so `SelectionContainer`
 * behaves like a `Box` for layout purposes — put a `Column` or `Row` inside when you need a
 * specific arrangement.
 */
@Immutable
@Serializable
@SerialName("SelectionContainer")
data class SelectionContainerTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility
) : TileSchema
