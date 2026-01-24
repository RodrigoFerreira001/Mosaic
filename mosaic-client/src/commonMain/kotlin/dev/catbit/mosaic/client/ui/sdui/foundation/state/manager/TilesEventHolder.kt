package dev.catbit.mosaic.client.ui.sdui.foundation.state.manager

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

interface TilesEventHolder {
    fun getEventsByTrigger(eventTrigger: EventTrigger): List<EventModel>?
}