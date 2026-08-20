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
 * Renders a read-only text field that opens a Material 3 `TimePickerDialog` when tapped.
 * [kind] picks the field composable: [Kind.FILLED] → `TextField`, [Kind.OUTLINED] →
 * `OutlinedTextField`. The field always shows an `alarm` leading icon, displays [selectedTime]
 * (empty when `null`), forwards [enabled] and [supportingText], and switches to Material's error
 * styling when [state] is [State.ERROR]. Typing is impossible — the field is `readOnly` and its
 * `onValueChange` is a no-op.
 *
 * **Times are ISO strings.** [selectedTime] is an ISO time (`HH:mm`); the renderer converts it
 * to/from hour + minute for the Compose `TimePickerState`. The dialog is always 24-hour and uses
 * the vertical layout; when [selectedTime] is `null` it opens at `00:00`.
 *
 * **Open state:** [expanded] drives the dialog. Pressing the field dispatches a local
 * `TimePickerTileEvents.OnTimePickerToggle` (holder flips [expanded]); confirming dispatches
 * `OnTimeConfirmed` (holder stores the time and closes); cancelling or dismissing dispatches
 * `OnTimePickerDismissRequest` (holder closes). All of this is handled client-side, without a
 * round trip to the server. The dialog's buttons are labelled with [confirmLabel] and
 * [cancelLabel].
 *
 * **Triggers dispatched:**
 * - `OnTimePickerOpenEventTrigger` — when the field is pressed while closed.
 * - `OnTimeSelectedEventTrigger` — when the confirm button is pressed; the ISO time string is
 *   passed as the event's incoming data.
 * - `OnTimePickerCloseEventTrigger` — when the field is pressed while open, when the dialog is
 *   dismissed, when cancel is pressed, and also right after a confirm.
 *
 * **Value production:** the holder exposes [selectedTime] under a caller-chosen key so
 * `GetData` / `EvaluateData` events can read this picker by its [id]. When no time is selected
 * it produces no entry at all.
 */
@Immutable
@Triggers(
    [
        OnTimePickerOpenEventTrigger::class,
        OnTimePickerCloseEventTrigger::class,
        OnTimeSelectedEventTrigger::class,
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
