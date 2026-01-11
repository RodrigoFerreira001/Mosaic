package dev.catbit.mosaic.core.data.event

import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

interface EventModel {
    val id: String
    val trigger: EventTrigger
    val events: List<EventModel>?
}