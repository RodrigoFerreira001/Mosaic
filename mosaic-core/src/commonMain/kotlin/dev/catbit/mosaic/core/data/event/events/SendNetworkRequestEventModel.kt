package dev.catbit.mosaic.core.data.event.events

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnStartEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("SendNetworkRequest")
data class SendNetworkRequestEventModel(
    val url: String,
    val method: HttpMethod,
    override val id: String,
    override val trigger: EventTrigger,
    override val events: List<EventModel>?,
) : EventModel {

    enum class HttpMethod {
        GET, POST, PUT, DELETE, UPDATE, PATCH
    }
}
