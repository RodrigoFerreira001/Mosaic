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
 * Renders a Material 3 [InputChip] displaying a discrete piece of information entered or chosen
 * by the user, such as a tag, a contact, or a filter value. Input chips appear in sets and are
 * typically horizontally scrollable or wrapped with [FlowRow].
 *
 * The chip supports a toggleable [selected] state and an optional [trailingIcon] (typically a
 * close/remove icon). The [leadingIcon] is shown at the start of the chip label.
 *
 * **Note:** The [trailingIcon] is purely visual — it is rendered inside the chip's single
 * clickable area. There is no separate trailing-icon click callback in the [InputChip] composable;
 * the chip's `onClick` covers the entire surface. Use the check/uncheck triggers to react to
 * taps and update state server-side (e.g. removing the chip via [UpdateTiles]).
 *
 * **Note:** The Material 3 [InputChip] composable does not have an elevated variant, so no
 * `variant` field is provided for this chip type.
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `text: String`, `selected: Boolean`,
 * `leadingIcon: IconSchema?`, `trailingIcon: IconSchema?`, `enabled: Boolean`
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
@SerialName("InputChip")
data class InputChipTileSchema(
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
) : TileSchema
