package dev.catbit.mosaic.core.data.schemas.event.events.scroll.pager

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.column.ScrollColumnTileEventSchema
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
@SerialName("ScrollPager")
data class ScrollPagerTileEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("tileId") val tileId: String,
    @SerialName("where") val where: Where,
    @SerialName("smoothly") val smoothly: Boolean
) : EventSchema {

    @Serializable
    sealed interface Where {
        @Serializable
        @SerialName("Begin")
        data object Begin : Where

        @Serializable
        @SerialName("PreviousPage")
        data object PreviousPage : Where

        @Serializable
        @SerialName("NextPage")
        data object NextPage : Where

        @Serializable
        @SerialName("End")
        data object End : Where
    }
}
