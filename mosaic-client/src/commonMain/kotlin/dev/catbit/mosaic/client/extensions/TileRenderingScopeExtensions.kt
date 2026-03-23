package dev.catbit.mosaic.client.extensions

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

fun TileRenderingScope.onClick(
    events: List<EventSchema>?
): (() -> Unit)? = events
    ?.takeIf { it.any { it.trigger == EventTriggers.onClick() } }
    ?.let { { triggerEvent(EventTriggers.onClick()) } }