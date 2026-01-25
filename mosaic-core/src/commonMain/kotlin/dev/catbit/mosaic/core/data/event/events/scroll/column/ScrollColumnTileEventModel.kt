package dev.catbit.mosaic.core.data.event.events.scroll.column

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnScrolledEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnScrolledEventTrigger::class
    ]
)
@Serializable
@SerialName("ScrollColumn")
data class ScrollColumnTileEventModel(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventModel>?,
    val tileId: String,
    val where: Where,
    val smoothly: Boolean
) : EventModel {

    sealed interface Where {
        data object Top : Where
        data class To(val index: Int) : Where
        data object Bottom : Where
    }
}
