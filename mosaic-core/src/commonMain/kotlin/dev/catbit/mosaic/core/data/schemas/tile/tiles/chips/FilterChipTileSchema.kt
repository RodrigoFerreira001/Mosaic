package dev.catbit.mosaic.core.data.schemas.tile.tiles.chips

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCheckChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnUncheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 [FilterChip] or [ElevatedFilterChip] displaying a text label with a
 * toggleable [selected] state. Filter chips use tags or descriptive words to filter content and
 * can be a good alternative to toggle buttons or checkboxes.
 *
 * Tapping the chip toggles the [selected] state. When [selected] is `true`, the chip renders
 * with a filled container; the [leadingIcon] can visually indicate selection (e.g. a checkmark).
 *
 * **Variants:**
 * - [Variant.DEFAULT] — flat chip with a border outline (default). Maps to [FilterChip].
 * - [Variant.ELEVATED] — elevated chip with shadow. Maps to [ElevatedFilterChip].
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `text: String`, `selected: Boolean`,
 * `leadingIcon: IconSchema?`, `trailingIcon: IconSchema?`, `enabled: Boolean`, `variant: Variant`
 *
 * **Triggers dispatched:**
 * - `OnCheckEventTrigger` — fired when the chip transitions to selected (`selected = true`).
 * - `OnUncheckEventTrigger` — fired when the chip transitions to unselected (`selected = false`).
 * - `OnCheckChangedEventTrigger` — fired on every selection state change.
 */
@Immutable
@Triggers(
    [
        OnCheckEventTrigger::class,
        OnUncheckEventTrigger::class,
        OnCheckChangedEventTrigger::class,
    ]
)
@Serializable
@SerialName("FilterChip")
data class FilterChipTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("text") val text: String,
    @SerialName("selected") val selected: Boolean,
    @SerialName("leadingIcon") val leadingIcon: IconSchema? = null,
    @SerialName("trailingIcon") val trailingIcon: IconSchema? = null,
    @SerialName("enabled") val enabled: Boolean,
    @SerialName("variant") val variant: Variant = Variant.DEFAULT,
) : TileSchema {

    /**
     * Visual style variant for the filter chip.
     *
     * - [DEFAULT] — flat chip with a border outline (maps to [FilterChip]).
     * - [ELEVATED] — elevated chip with shadow (maps to [ElevatedFilterChip]).
     */
    enum class Variant { DEFAULT, ELEVATED }
}
