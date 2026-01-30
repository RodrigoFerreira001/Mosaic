package dev.catbit.mosaic.core.data.schemas.event.events.scroll.column

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnScrolledEventTrigger::class
    ]
)
@Serializable
@SerialName("ScrollColumn")
data class ScrollColumnTileEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    val tileId: String,
    val where: Where,
    val smoothly: Boolean
) : EventSchema {

    sealed interface Where {
        data object Top : Where
        data class To(val index: Int) : Where
        data object Bottom : Where
    }
}
