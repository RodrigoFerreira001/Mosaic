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
 * Renders a read-only text field that opens a Material 3 `DatePickerDialog` when tapped.
 * [kind] picks the field composable: [Kind.FILLED] → `TextField`, [Kind.OUTLINED] →
 * `OutlinedTextField`. The field always shows a `calendar_month` leading icon, displays
 * [selectedDate] (empty when `null`), forwards [enabled] and [supportingText], and switches to
 * Material's error styling when [state] is [State.ERROR]. Typing is impossible — the field is
 * `readOnly` and its `onValueChange` is a no-op.
 *
 * **Dates are ISO strings.** [selectedDate] is an ISO date (`yyyy-MM-dd`); the renderer converts
 * it to/from epoch millis for the Compose `DatePickerState`.
 *
 * **Open state:** [expanded] drives the dialog. Pressing the field dispatches a local
 * `DatePickerTileEvents.OnDatePickerToggle` (holder flips [expanded]); confirming dispatches
 * `OnDateConfirmed` (holder stores the date and closes); cancelling or dismissing dispatches
 * `OnDatePickerDismissRequest` (holder closes). All of this is handled client-side, without a
 * round trip to the server. The dialog's buttons are labelled with [confirmLabel] and
 * [cancelLabel], and the confirm button stays disabled until a date is picked, so confirming
 * always yields a date.
 *
 * **Triggers dispatched:**
 * - `OnDatePickerOpenEventTrigger` — when the field is pressed while closed.
 * - `OnDateSelectedEventTrigger` — when the confirm button is pressed; the ISO date string is
 *   passed as the event's incoming data.
 * - `OnDatePickerCloseEventTrigger` — when the field is pressed while open, when the dialog is
 *   dismissed, when cancel is pressed, and also right after a confirm.
 *
 * **Value production:** the holder exposes [selectedDate] under a caller-chosen key so
 * `GetData` / `EvaluateData` events can read this picker by its [id]. When no date is selected
 * it produces no entry at all.
 */
@Immutable
@Triggers(
    [
        OnDatePickerOpenEventTrigger::class,
        OnDatePickerCloseEventTrigger::class,
        OnDateSelectedEventTrigger::class,
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
