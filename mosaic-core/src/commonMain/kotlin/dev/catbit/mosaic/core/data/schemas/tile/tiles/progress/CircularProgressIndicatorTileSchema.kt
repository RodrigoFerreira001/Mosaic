package dev.catbit.mosaic.core.data.schemas.tile.tiles.progress

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 [CircularProgressIndicator]. When [progress] is non-null, a
 * determinate indicator is rendered showing the given fraction (0.0–1.0). When [progress] is
 * `null`, an indeterminate (spinning) indicator is rendered instead.
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `progress: Float?`
 *
 * **Triggers dispatched:** None. This tile does not dispatch any event triggers.
 *
 * **Notes:** The determinate vs. indeterminate mode is toggled purely by the presence or
 * absence of [progress]. To transition from indeterminate to determinate, send an UpdateTiles
 * event with a non-null [progress] value. The [progress] value is read as a lambda
 * (`progress = { it }`) so that Compose can skip recomposition if only the value changes
 * without structural layout changes.
 */
@Immutable
@Serializable
@SerialName("CircularProgressIndicator")
data class CircularProgressIndicatorTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("progress") val progress: Float? = null
) : TileSchema
