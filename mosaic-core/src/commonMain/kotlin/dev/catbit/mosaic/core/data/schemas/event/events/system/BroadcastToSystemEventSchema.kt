package dev.catbit.mosaic.core.data.schemas.event.events.system

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Emits a named broadcast via [SystemBroadcastChannel], allowing any subscriber in the app to
 * react to a `broadcastId`/payload pair without direct coupling to the event chain.
 *
 * **incomingData consumed:** Only when [data] is [BroadcastData.Incoming]. The current
 * `incomingData` is forwarded as the broadcast payload. If `incomingData` is `null` in that case,
 * no broadcast is emitted and [OnFailureEventTrigger] fires instead.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — fired after the broadcast is emitted successfully.
 * - [OnFailureEventTrigger] — fired when [data] is [BroadcastData.Incoming] and `incomingData`
 *   is `null`.
 *
 * **Failure scenarios:**
 * - [BroadcastData.Incoming] with `incomingData == null`: fires [OnFailureEventTrigger], no
 *   broadcast is emitted.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
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
