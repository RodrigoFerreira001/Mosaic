package dev.catbit.mosaic.core.data.event.events.overlays.dialog

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnDialogDismissedEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnDialogDismissedEventTrigger::class
    ]
)
@Serializable
@SerialName("CloseBottomSheet")
data class DismissDialogEventModel(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventModel>?
) : EventModel