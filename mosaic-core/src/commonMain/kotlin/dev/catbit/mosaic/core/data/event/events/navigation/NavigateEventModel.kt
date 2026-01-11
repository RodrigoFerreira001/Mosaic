package dev.catbit.mosaic.core.data.event.events.navigation

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Navigate")
data class NavigateEventModel(
    val destination: String,
    val navigatorId: String,
    val popUpTo: PopUpTo?,
    val data: Map<String, AnySerializable>?,
    override val trigger: EventTrigger,
    override val id: String,
    override val events: List<EventModel>?,
) : EventModel {

    @Serializable
    data class PopUpTo(
        val destination: String,
        val inclusive: Boolean
    )
}