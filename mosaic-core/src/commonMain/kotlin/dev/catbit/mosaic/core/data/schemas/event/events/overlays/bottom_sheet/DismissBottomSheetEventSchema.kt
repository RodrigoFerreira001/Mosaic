package dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Dismisses a non-modal bottom sheet previously opened by a [DisplayBottomSheetEventSchema] — the
 * "reaction" half of the pair. No tile data is altered.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — fired once the sheet is marked for dismissal. The sheet then plays
 *   its exit animation and leaves the overlay stack.
 * - [OnFailureEventTrigger] — fired with the offending id when no sheet with [bottomSheetId] is
 *   on the stack.
 *
 * **Failure scenarios:** Dismissing a [bottomSheetId] that is not currently on the overlay stack
 * fails, which also covers dismissing the same sheet twice.
 *
 * **Notes:** [bottomSheetId] must match the id given to the [DisplayBottomSheetEventSchema] that
 * opened the sheet. Because overlays are stackable, this is what allows a sheet buried under other
 * overlays to be closed without touching the ones above it.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("DismissBottomSheet")
data class DismissBottomSheetEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("bottomSheetId") val bottomSheetId: String,
) : EventSchema
