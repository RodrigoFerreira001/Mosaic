package dev.catbit.mosaic.core.data.event.events.overlays.bottom_sheet

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnBottomSheetDismissedEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnBottomSheetDismissedEventTrigger::class
    ]
)
@Serializable
@SerialName("DismissBottomSheet")
data class DismissBottomSheetEventModel(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventModel>?
) : EventModel