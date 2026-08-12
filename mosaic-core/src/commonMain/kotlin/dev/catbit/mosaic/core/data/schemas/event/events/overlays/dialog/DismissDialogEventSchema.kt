package dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Dismisses a dialog previously opened by a [DisplayDialogEventSchema] — the "reaction" half of
 * the pair. No tile data is altered.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — fired once the dialog is marked for dismissal and leaves the
 *   overlay stack.
 * - [OnFailureEventTrigger] — fired with the offending id when no dialog with [dialogId] is on
 *   the stack.
 *
 * **Failure scenarios:** Dismissing a [dialogId] that is not currently on the overlay stack
 * fails, which also covers dismissing the same dialog twice.
 *
 * **Notes:** [dialogId] must match the id given to the [DisplayDialogEventSchema] that opened the
 * dialog. Because overlays are stackable, this is what allows a dialog buried under other overlays
 * to be closed without touching the ones above it.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("DismissDialog")
data class DismissDialogEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("dialogId") val dialogId: String,
) : EventSchema