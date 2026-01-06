package dev.catbit.mosaic.core.data.event.events.overlays

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("CloseBottomSheet")
data class CloseDialogEventModel(
    override val trigger: EventTrigger,
    override val id: String,
    override val events: List<EventModel>?,
) : EventModel