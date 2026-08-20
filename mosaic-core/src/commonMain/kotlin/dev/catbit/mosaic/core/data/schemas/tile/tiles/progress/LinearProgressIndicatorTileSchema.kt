package dev.catbit.mosaic.core.data.schemas.tile.tiles.progress

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 `LinearProgressIndicator`. When [progress] is `null` the indicator is
 * indeterminate (endless sweep); when set, it is determinate and shows that fraction, where
 * `0f` is empty and `1f` is complete.
 *
 * **Triggers dispatched:** none. The tile emits no trigger and is not clickable, so any `events`
 * declared on it are never fired.
 *
 * **Notes:** colors, track and stroke cap are Material defaults — only [style] (size, padding,
 * background, …) is applied. Drive a determinate indicator by pushing new [progress] values with
 * `UpdateTiles`.
 */
@Immutable
@Serializable
@SerialName("LinearProgressIndicator")
data class LinearProgressIndicatorTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("progress") val progress: Float? = null
) : TileSchema
