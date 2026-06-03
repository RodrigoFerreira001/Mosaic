package dev.catbit.mosaic.core.data.schemas.tile.tiles.chips

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a Material 3 [SuggestionChip] displaying a text label. The chip can be enabled or
 * disabled via [enabled]. When tapped, it dispatches a click trigger to its child events.
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `text: String`, `enabled: Boolean`
 *
 * **Triggers dispatched:** `OnClickEventTrigger` — fired when the chip is tapped (only when
 * [enabled] is `true`; disabled chips do not fire click events).
 *
 * **Notes:** The chip label is always a plain [Text] composable; rich content in the label
 * is not supported. There is no leading icon slot in the current renderer, even though
 * Material 3 [SuggestionChip] supports one.
 */
@Triggers(
    [
        OnClickEventTrigger::class
    ]
)
@Serializable
@SerialName("SuggestionChip")
data class SuggestionChipTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("text") val text: String,
    @SerialName("enabled") val enabled: Boolean,
) : TileSchema
