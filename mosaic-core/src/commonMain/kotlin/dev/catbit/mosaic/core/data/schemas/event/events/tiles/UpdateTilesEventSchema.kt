package dev.catbit.mosaic.core.data.schemas.event.events.tiles

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesUpdatedEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnTilesUpdatedEventTrigger::class
    ]
)
@Serializable
@SerialName("UpdateTiles")
data class UpdateTilesEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("updates") val updates: List<Update>,
) : EventSchema {

    @Serializable
    data class Update(
        @SerialName("tileId") val tileId: String,
        @SerialName("data") val data: Map<String, AnySerializable?>
    )
}