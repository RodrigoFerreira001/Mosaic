package dev.catbit.mosaic.core.data.schemas.event.events.system

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Publishes a value on the client's system broadcast channel under [broadcastId], where the host
 * application and any mounted `SystemBroadcastListener` tile can pick it up. This is the outbound
 * half of the bridge between server-declared flows and native app code.
 *
 * [data] chooses the payload: `Inline` publishes a literal declared on the event, `Incoming`
 * publishes the event's incomingData.
 *
 * **incomingData consumed:** published as the payload when [data] is `Incoming`.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — after the value was published. No data is passed downstream.
 * - `OnFailureEventTrigger` — only in the `Incoming` case, when incomingData is `null` so there is
 *   nothing to publish; no data is passed downstream.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("BroadcastToSystem")
data class BroadcastToSystemEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("broadcastId") val broadcastId: String,
    @SerialName("data") val data: BroadcastData
) : EventSchema {

    @Serializable
    sealed interface BroadcastData {
        @Serializable
        @SerialName("Incoming")
        data object Incoming : BroadcastData

        @Serializable
        @SerialName("Inline")
        data class Inline(
            @SerialName("data") val data: AnySerializable
        ) : BroadcastData
    }
}
