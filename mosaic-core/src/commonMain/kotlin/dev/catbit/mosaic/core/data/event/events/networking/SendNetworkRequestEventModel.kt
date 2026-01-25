package dev.catbit.mosaic.core.data.event.events.networking

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnNetworkResponseTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnStartEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
        OnNetworkResponseTrigger::class
    ]
)
@Serializable
@SerialName("SendNetworkRequest")
data class SendNetworkRequestEventModel(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventModel>?,
    val url: String,
    val method: HttpMethod,
    val body: AnySerializable?,
    val headers: Map<String, String>?
) : EventModel {

    enum class HttpMethod {
        GET, POST, PUT, DELETE, UPDATE, PATCH
    }
}