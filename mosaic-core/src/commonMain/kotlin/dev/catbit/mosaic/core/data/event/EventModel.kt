package dev.catbit.mosaic.core.data.event

import dev.catbit.mosaic.core.trigger.Trigger

interface EventModel {
    val id: String
    val trigger: Trigger
    val events: List<EventModel>?
}