package dev.catbit.mosaic.core.data.schemas.event

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

@Immutable
interface EventSchema {
    val id: String
    val trigger: EventTrigger
    val events: SerializableImmutableList<EventSchema>?
}