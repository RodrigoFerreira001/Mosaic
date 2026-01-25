package dev.catbit.mosaic.core.data.event.events.overlays.navigation_drawer

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("DisplayNavigationDrawer")
data class DisplayNavigationDrawerEventModel(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventModel>?
) : EventModel