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
 * Renders a Material3 `TooltipBox` wrapping the anchor content ([tiles]). The tooltip itself is a
 * `PlainTooltip` showing [text]. `TooltipBox` handles gesture detection (hover on desktop/web,
 * long-press on touch) and dismissal internally — there is no server-driven expanded state and no
 * dismiss event, unlike [dev.catbit.mosaic.core.data.schemas.tile.tiles.popup.PopupTileSchema] or
 * [dev.catbit.mosaic.core.data.schemas.tile.tiles.menu.MenuTileSchema].
 *
 * [position] controls where the tooltip is placed relative to the anchor; [spacing] is the gap
 * between them in dp — `null` falls back to Compose Material3's own default spacing.
 * [showCaret] toggles `TooltipDefaults.caretShape()` (the caret is a dedicated triangular shape
 * combined with [shape] by `PlainTooltip` — it can't be represented by [ShapeSchema], which only
 * models plain container shapes). [maxWidth], [shape], [contentColor] and [containerColor] mirror
 * `PlainTooltip`'s remaining appearance parameters; a `null` value falls back to the Material3
 * default for that parameter.
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
