package dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDatePickerCloseEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDatePickerOpenEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDateSelectedEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a Material 3 `TextField`-like input that opens a `DatePickerDialog` when tapped, in
 * either a filled or outlined visual style controlled by [kind].
 *
 * **Form tile:** [produceValueWithKey] returns `mapOf(key to selectedDate)`. [selectedDate] is
 * nullable — `null` represents "no date selected".
 *
 * **Server contract:** the server always sends [expanded] as `false`; the client owns the
 * open/closed dialog state. [selectedDate] is an ISO-8601 date string (e.g. `"2026-07-07"`).
 *
 * **Updatable fields (via UpdateTiles):** `selectedDate`, `enabled`, `kind`, `dialogOptions`,
 * `supportingText`, `state`, `visibility`, `style`.
 *
 * **Triggers dispatched:**
 * - [OnDateSelectedEventTrigger] — fired when the user confirms a date in the dialog; carries
 *   the selected date as incomingData (ISO-8601 string).
 * - [OnDatePickerOpenEventTrigger] — fired when the dialog opens.
 * - [OnDatePickerCloseEventTrigger] — fired when the dialog closes (confirm, dismiss button, or
 *   outside-dismiss).
 *
 * **Client state management:**
 * - `OnDatePickerToggle` TileEvent — toggles [expanded].
 * - `OnDatePickerDismissRequest` TileEvent — collapses [expanded] to `false`.
 * - `OnDateConfirmed` TileEvent — updates [selectedDate] and collapses [expanded] to `false`.
 */
@Immutable
@Triggers(
    [
        OnDateSelectedEventTrigger::class,
        OnDatePickerOpenEventTrigger::class,
        OnDatePickerCloseEventTrigger::class,
    ]
)
@Serializable
@SerialName("DatePicker")
data class DatePickerTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("expanded") val expanded: Boolean,
    @SerialName("selectedDate") val selectedDate: String?,
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
