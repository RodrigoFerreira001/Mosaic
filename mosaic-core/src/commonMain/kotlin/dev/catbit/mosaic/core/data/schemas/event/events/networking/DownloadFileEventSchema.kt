package dev.catbit.mosaic.core.data.schemas.event.events.networking

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFinishEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadProgressEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnStartEventTrigger::class,
        OnDownloadFailureEventTrigger::class,
        OnDownloadFinishEventTrigger::class,
        OnDownloadProgressEventTrigger::class,
    ]
)
@Serializable
@SerialName("DownloadFile")
data class DownloadFileEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    val url: String,
    val method: HttpMethod,
    val body: AnySerializable?,
    val headers: Map<String, String>?
) : EventSchema {

    enum class HttpMethod {
        GET, POST, PUT, DELETE, UPDATE, PATCH
    }
}