package dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTimePickerCloseEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTimePickerOpenEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTimeSelectedEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a Material 3 `TextField`-like input that opens a `TimePickerDialog` when tapped, in
 * either a filled or outlined visual style controlled by [kind].
 *
 * **Form tile:** [produceValueWithKey] returns `mapOf(key to selectedTime)`. [selectedTime] is
 * nullable — `null` represents "no time selected".
 *
 * **Server contract:** the server always sends [expanded] as `false`; the client owns the
 * open/closed dialog state. [selectedTime] is a `"HH:mm"` string (24h format).
 *
 * **Updatable fields (via UpdateTiles):** `selectedTime`, `enabled`, `kind`, `dialogOptions`,
 * `supportingText`, `state`, `visibility`, `style`.
 *
 * **Triggers dispatched:**
 * - [OnTimeSelectedEventTrigger] — fired when the user confirms a time in the dialog; carries
 *   the selected time as incomingData (`"HH:mm"` string).
 * - [OnTimePickerOpenEventTrigger] — fired when the dialog opens.
 * - [OnTimePickerCloseEventTrigger] — fired when the dialog closes (confirm, dismiss button, or
 *   outside-dismiss).
 *
 * **Client state management:**
 * - `OnTimePickerToggle` TileEvent — toggles [expanded].
 * - `OnTimePickerDismissRequest` TileEvent — collapses [expanded] to `false`.
 * - `OnTimeConfirmed` TileEvent — updates [selectedTime] and collapses [expanded] to `false`.
 */
@Immutable
@Triggers(
    [
        OnTimeSelectedEventTrigger::class,
        OnTimePickerOpenEventTrigger::class,
        OnTimePickerCloseEventTrigger::class,
    ]
)
@Serializable
@SerialName("TimePicker")
data class TimePickerTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("expanded") val expanded: Boolean,
    @SerialName("selectedTime") val selectedTime: String?,
    @SerialName("enabled") val enabled: Boolean,
    @SerialName("kind") val kind: Kind,
    @SerialName("confirmLabel") val confirmLabel: String,
    @SerialName("cancelLabel") val cancelLabel: String,
    @SerialName("supportingText") val supportingText: String?,
    @SerialName("state") val state: State
) : TileSchema {

    enum class Kind {
        FILLED, OUTLINED
    }

    enum class State {
        NORMAL, ERROR
    }
}
