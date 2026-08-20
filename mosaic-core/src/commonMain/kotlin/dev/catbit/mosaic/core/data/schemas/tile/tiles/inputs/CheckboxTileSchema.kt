package dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCheckChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnUncheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a bare Material 3 `Checkbox` reflecting [checked]. [enabled] is forwarded to the
 * composable. The tile draws only the box — no label is rendered, so pair it with a
 * `SimpleText` inside a `Row` when a caption is needed.
 *
 * **Selection:** toggling the checkbox dispatches a local `CheckboxTileEvents.OnCheckChanged`
 * that the holder applies to its own state, so the new value survives without a round trip
 * to the server.
 *
 * **Triggers dispatched (in this order, on every toggle):**
 * - `OnCheckEventTrigger` — when the box becomes checked.
 * - `OnUncheckEventTrigger` — when the box becomes unchecked.
 * - `OnCheckChangedEventTrigger` — always, right after one of the two above.
 *
 * **Value production:** the holder exposes the current [checked] boolean under a caller-chosen
 * key, so `GetData` / `EvaluateData` events can read this checkbox by its [id].
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
@SerialName("Checkbox")
data class CheckboxTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("checked") val checked: Boolean,
    @SerialName("enabled") val enabled: Boolean,
) : TileSchema
