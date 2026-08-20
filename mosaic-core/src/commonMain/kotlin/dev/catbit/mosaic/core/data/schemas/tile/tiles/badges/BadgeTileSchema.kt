package dev.catbit.mosaic.core.data.schemas.tile.tiles.badges

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 `Badge`. When [content] is non-null it is rendered as the badge's text
 * label and the badge takes its larger, pill-shaped form; when `null` the badge is the small
 * empty dot.
 *
 * **Triggers dispatched:** none. The badge emits no trigger and is not clickable, so any
 * `events` declared on it are never fired.
 *
 * **Notes:** this tile is a standalone badge, not a `BadgedBox` — it does not attach itself to a
 * sibling. Position it yourself, e.g. inside a `Box` aligned over the tile it decorates.
 */
@Immutable
@Serializable
@SerialName("Badge")
data class BadgeTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("content") val content: String? = null
) : TileSchema
