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
 * Renders a Material 3 `InputChip` with a toggleable [selected] state, [text] as its label and
 * optional [leadingIcon] / [trailingIcon]. [enabled] is forwarded to the underlying composable.
 * This tile has no variants — it always maps to the flat `InputChip`.
 *
 * **Selection:** tapping the chip flips [selected]. The renderer dispatches a local
 * `InputChipTileEvents.OnCheckChanged` that the holder applies to its own state, so the new
 * value survives without a round trip to the server.
 *
 * **Triggers dispatched (in this order, on every tap):**
 * - `OnCheckEventTrigger` — when the chip becomes selected.
 * - `OnUncheckEventTrigger` — when the chip becomes unselected.
 * - `OnCheckChangedEventTrigger` — always, right after one of the two above.
 *
 * **Value production:** the holder exposes the current [selected] boolean under a caller-chosen
 * key, so `GetData` / `EvaluateData` events can read this chip by its [id].
 *
 * **Notes:** the trailing icon is decorative — it does not fire a separate dismiss trigger;
 * tapping anywhere on the chip toggles it.
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
