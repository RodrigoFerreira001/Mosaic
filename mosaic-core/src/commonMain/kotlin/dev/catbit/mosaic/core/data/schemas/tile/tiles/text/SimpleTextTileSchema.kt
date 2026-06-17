package dev.catbit.mosaic.core.data.schemas.tile.tiles.text

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.data.schemas.typography.TypographySchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a non-interactive text label using Compose's [Text] composable.
 *
 * **Updatable fields (via UpdateTiles):** `text`, `color`, `typography`, `visibility`, `style`.
 *
 * **Triggers dispatched:** None — this tile has no `@Triggers` annotation and fires no events
 * in response to user interaction.
 *
 * **Notes:** [color] accepts a nullable [ColorSchema]; when null the renderer falls back to
 * [LocalTextStyle]'s inherited color (`Color.Unspecified`). [typography] maps to a Compose
 * [TextStyle] via `toTextStyle()`; when null the ambient [LocalTextStyle] is used unchanged.
 * The tile respects [visibility] via the `visible()` modifier — the composable is still laid
 * out but hidden when invisible, preserving its space in the parent layout.
 */
@Serializable
@SerialName("Text")
@Immutable
data class SimpleTextTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("text") val text: String,
    @SerialName("color") val color: ColorSchema?,
    @SerialName("typography") val typography: TypographySchema?
) : TileSchema