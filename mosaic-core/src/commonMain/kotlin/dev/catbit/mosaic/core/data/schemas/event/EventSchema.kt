package dev.catbit.mosaic.core.data.schemas.event

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger

interface EventSchema {
    val id: String
    val trigger: EventTrigger
    val events: List<EventSchema>?
}