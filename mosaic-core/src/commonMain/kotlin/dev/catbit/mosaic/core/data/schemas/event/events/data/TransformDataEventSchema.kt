package dev.catbit.mosaic.core.data.schemas.event.events.data

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
 * Reshapes the event's incomingData into a new value by applying [template] through the client's
 * `TemplateProcessor`, letting a payload be rewritten mid-chain without a round trip.
 *
 * **incomingData consumed:** it is the input the template is applied to.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the template was applied; the transformed value is passed as
 *   incomingData.
 * - `OnFailureEventTrigger` — when applying the template throws; the `Throwable` is passed as
 *   incomingData and the error is logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("TransformData")
data class TransformDataEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("template") val template: AnySerializable
) : EventSchema