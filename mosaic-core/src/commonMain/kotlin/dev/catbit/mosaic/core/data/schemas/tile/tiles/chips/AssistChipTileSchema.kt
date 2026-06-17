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
 * Renders a Material 3 [AssistChip] or [ElevatedAssistChip] displaying a text label with
 * optional leading and trailing icons. Assist chips represent smart or automated actions that
 * can span multiple apps, such as opening a calendar event from the home screen.
 *
 * The chip can be enabled or disabled via [enabled]. When tapped, it dispatches a click trigger
 * to its child events.
 *
 * **Use case example:** a chip with a calendar icon as [leadingIcon] and a chevron-down as
 * [trailingIcon] to open a date-picker menu (like Google Drive's "Type" filter chip).
 *
 * **Variants:**
 * - [Variant.DEFAULT] — flat chip with a border outline (default). Maps to [AssistChip].
 * - [Variant.ELEVATED] — elevated chip with shadow, no border. Maps to [ElevatedAssistChip].
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `text: String`, `leadingIcon: IconSchema?`,
 * `trailingIcon: IconSchema?`, `enabled: Boolean`, `variant: Variant`
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
@SerialName("AssistChip")
data class AssistChipTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("text") val text: String,
    @SerialName("leadingIcon") val leadingIcon: IconSchema? = null,
    @SerialName("trailingIcon") val trailingIcon: IconSchema? = null,
    @SerialName("enabled") val enabled: Boolean,
    @SerialName("variant") val variant: Variant = Variant.DEFAULT,
) : TileSchema {

    /**
     * Visual style variant for the assist chip.
     *
     * - [DEFAULT] — flat chip with a border outline (maps to [AssistChip]).
     * - [ELEVATED] — elevated chip with shadow and no border (maps to [ElevatedAssistChip]).
     */
    enum class Variant { DEFAULT, ELEVATED }
}
