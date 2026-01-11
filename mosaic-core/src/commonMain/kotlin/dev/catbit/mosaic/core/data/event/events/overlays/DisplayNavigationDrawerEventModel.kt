package dev.catbit.mosaic.core.data.event.events.overlays

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("DisplayNavigationDrawer")
data class DisplayNavigationDrawerEventModel(
    override val trigger: EventTrigger,
    override val id: String,
    override val events: List<EventModel>?,
) : EventModel