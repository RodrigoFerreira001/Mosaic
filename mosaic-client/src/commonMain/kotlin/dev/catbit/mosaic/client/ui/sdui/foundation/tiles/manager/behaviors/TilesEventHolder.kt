package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger

/**
 * Surface that lets `EventManager` ask a screen's `TilesManager` which currently-registered
 * [EventSchema]s match a given [EventTrigger], without `EventManager` needing to know anything about
 * the shape of the tile tree itself — the collaborator behind `EventManager.triggerEvents`, which is
 * how a screen-level trigger like `onDisplay()` finds and runs every matching event anywhere in the
 * tree.
 */
interface TilesEventHolder {
    /**
     * Every [EventSchema] currently registered anywhere in this screen's tile tree whose `trigger`
     * equals [eventTrigger].
     *
     * @param eventTrigger the trigger to match against.
     * @return every matching [EventSchema] found, or `null`/empty depending on the implementation
     * when none match.
     */
    fun getEventsByTrigger(eventTrigger: EventTrigger): List<EventSchema>?
}