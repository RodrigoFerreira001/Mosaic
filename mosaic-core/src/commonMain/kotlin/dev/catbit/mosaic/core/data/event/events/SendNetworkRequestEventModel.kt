package dev.catbit.mosaic.core.data.event.events

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.trigger.EventTrigger
import dev.catbit.mosaic.core.trigger.triggers.OnFailureTrigger
import dev.catbit.mosaic.core.trigger.triggers.OnStartTrigger
import dev.catbit.mosaic.core.trigger.triggers.OnSuccessTrigger
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnStartTrigger::class,
        OnSuccessTrigger::class,
        OnFailureTrigger::class
    ]
)
@Serializable
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
