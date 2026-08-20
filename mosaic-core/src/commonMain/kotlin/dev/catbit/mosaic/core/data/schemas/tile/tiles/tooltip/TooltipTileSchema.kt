package dev.catbit.mosaic.core.data.schemas.tile.tiles.tooltip

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.ShapeSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Wraps [tiles] in a Material 3 `TooltipBox` that shows a `PlainTooltip` with [text]. The
 * tooltip is driven entirely by the platform's own gestures (long press on touch, hover on
 * pointer devices) through a remembered tooltip state — the server neither opens nor observes
 * it.
 *
 * **Appearance:** [position] maps onto the Compose `TooltipAnchorPosition` of the same name and
 * [spacing] (dp) sets the gap from the anchor, defaulting to Material's own spacing when `null`.
 * [showCaret] toggles the little pointer arrow, and [maxWidth], [shape], [contentColor] and
 * [containerColor] each override the corresponding `TooltipDefaults` value when non-null.
 *
 * **Triggers dispatched:** none. The tile emits no trigger of its own, so any `events` declared
 * on it are never fired — wire events on the wrapped tiles instead.
 *
 * **Notes:** [style] and [visibility] apply to the `TooltipBox` itself — that is, to the anchor
 * — not to the tooltip surface, whose look is controlled by the fields above. Only plain
 * tooltips are supported; there is no rich tooltip variant with a title and actions.
 */
@Immutable
@Serializable
@SerialName("Tooltip")
data class TooltipTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("text") val text: String,
    @SerialName("position") val position: Position,
    @SerialName("spacing") val spacing: Int?,
    @SerialName("showCaret") val showCaret: Boolean,
    @SerialName("maxWidth") val maxWidth: Int?,
    @SerialName("shape") val shape: ShapeSchema?,
    @SerialName("contentColor") val contentColor: ColorSchema?,
    @SerialName("containerColor") val containerColor: ColorSchema?
) : TileSchema {

    @Serializable
    enum class Position {
        @SerialName("above") ABOVE,
        @SerialName("below") BELOW,
        @SerialName("left") LEFT,
        @SerialName("right") RIGHT,
        @SerialName("start") START,
        @SerialName("end") END
    }
}
