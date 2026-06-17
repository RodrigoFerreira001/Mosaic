package dev.catbit.mosaic.core.data.schemas.tile.tiles.chips

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 [SuggestionChip] or [ElevatedSuggestionChip] displaying a text label.
 * Suggestion chips help narrow a user's intent by presenting dynamically generated suggestions,
 * such as possible responses or search filters.
 *
 * The chip can be enabled or disabled via [enabled]. When tapped, it dispatches a click trigger
 * to its child events.
 *
 * **Variants:**
 * - [Variant.DEFAULT] — flat chip with a border outline (default).
 * - [Variant.ELEVATED] — elevated chip with shadow, no border. Maps to [ElevatedSuggestionChip].
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `text: String`, `icon: IconSchema?`,
 * `enabled: Boolean`, `variant: Variant`
 *
 * **Triggers dispatched:** `OnClickEventTrigger` — fired when the chip is tapped (only when
 * [enabled] is `true`; disabled chips do not fire click events).
 */
@Immutable
@Triggers(
    [
        OnClickEventTrigger::class
    ]
)
@Serializable
@SerialName("SuggestionChip")
data class SuggestionChipTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("text") val text: String,
    @SerialName("icon") val icon: IconSchema? = null,
    @SerialName("enabled") val enabled: Boolean,
    @SerialName("variant") val variant: Variant = Variant.DEFAULT,
) : TileSchema {

    /**
     * Visual style variant for the suggestion chip.
     *
     * - [DEFAULT] — flat chip with a border outline (maps to [SuggestionChip]).
     * - [ELEVATED] — elevated chip with shadow and no border (maps to [ElevatedSuggestionChip]).
     */
    enum class Variant { DEFAULT, ELEVATED }
}
