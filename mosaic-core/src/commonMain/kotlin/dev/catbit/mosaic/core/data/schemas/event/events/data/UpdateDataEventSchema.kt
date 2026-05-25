package dev.catbit.mosaic.core.data.schemas.event.events.data

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataUpdatedEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnDataUpdatedEventTrigger::class
    ]
)
@Serializable
@SerialName("UpdateData")
data class UpdateDataEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("updates") val updates: List<Update>
) : EventSchema {

    @Serializable
    data class Update(
        @SerialName("dataSource") val dataSource: DataSourceSchema,
        @SerialName("updateData") val updateData: UpdateDate
    ) {
        @Serializable
        sealed interface UpdateDate {
            @Serializable
            @SerialName("Incoming")
            data object Incoming : UpdateDate

            @Serializable
            @SerialName("Inline")
            data class Inline(
                @SerialName("data")
                val data: Map<String, AnySerializable>
            ) : UpdateDate
        }
    }
}