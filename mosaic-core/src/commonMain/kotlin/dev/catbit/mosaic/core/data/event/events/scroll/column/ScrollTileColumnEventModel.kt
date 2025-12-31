package dev.catbit.mosaic.core.data.event.events.scroll.column

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ScrollColumn")
data class ScrollTileColumnEventModel(
    val tileId: String,
    val where: Where,
    val smooth: Boolean,
    override val id: String,
    override val trigger: EventTrigger,
    override val events: List<EventModel>?
) : EventModel {

    sealed interface Where {
        data object Top : Where
        data class To(val index: Int) : Where
        data object Bottom : Where
    }
}
