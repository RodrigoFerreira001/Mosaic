package dev.catbit.mosaic.client.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

/**
 * Returns a callback firing [trigger], or `null` when [events] declares no event wired to it —
 * so a tile is only made interactive when something is actually listening.
 */
fun TileRenderingScope.callbackFor(
    trigger: EventTrigger,
    events: List<EventSchema>?
): (() -> Unit)? = events
    ?.takeIf { it.any { event -> event.trigger == trigger } }
    ?.let { { triggerEvent(trigger) } }

/** [callbackFor] specialized for `EventTriggers.onClick()` — the standard way a tile renderer wires
 * up `Modifier.styledWith`'s `onClick`/a composable's own `onClick` parameter, only when the tile
 * actually declares an `OnClick` event. */
fun TileRenderingScope.onClick(
    events: List<EventSchema>?
): (() -> Unit)? = callbackFor(EventTriggers.onClick(), events)

/** [callbackFor] specialized for `EventTriggers.onLongPress()` — same pattern as [onClick], for
 * `Modifier.styledWith`'s `onLongClick`. */
fun TileRenderingScope.onLongPress(
    events: List<EventSchema>?
): (() -> Unit)? = callbackFor(EventTriggers.onLongPress(), events)

/**
 * Fires `EventTriggers.onDisplay()` once, the first time this composable enters composition — the
 * standard way every built-in container/interactive tile implements its own `OnDisplay` trigger.
 * Keyed on `tileId`, so it re-fires only if the tile identity itself changes (a new instance with a
 * different id), not on every recomposition.
 */
@Composable
fun TileRenderingScope.OnDisplayEffect() {
    LaunchedEffect(tileId) {
        triggerEvent(EventTriggers.onDisplay())
    }
}
