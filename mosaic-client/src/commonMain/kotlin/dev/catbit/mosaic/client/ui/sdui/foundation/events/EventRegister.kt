package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.core.data.event.EventModel

interface EventRegister {
    fun registerEvents(
        eventOwnerId: String,
        eventList: List<EventModel>
    )

    fun unregisterEvents(
        eventOwnerId: String,
    )
}