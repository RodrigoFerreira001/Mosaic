package dev.catbit.mosaic.core.data.event.events.menu

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ToggleMenu")
data class ToggleMenuEventModel(
    override val id: String,
    override val trigger: EventTrigger,
    override val events: List<EventModel>?,
    val menuId: String
) : EventModel
