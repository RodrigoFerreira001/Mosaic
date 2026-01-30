package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger

interface TilesEventHolder {
    fun getEventsByTrigger(eventTrigger: EventTrigger): List<EventSchema>?
}