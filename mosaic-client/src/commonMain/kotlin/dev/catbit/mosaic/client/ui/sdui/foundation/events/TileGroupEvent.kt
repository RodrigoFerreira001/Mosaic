package dev.catbit.mosaic.client.ui.sdui.foundation.events

/**
 * Marker interface for an event broadcast to every tile in the tree that opts in to receiving it —
 * the payload of
 * [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope.dispatchGroupEvent],
 * delivered to every `TileHolder` whose `handlesGroupEvent(event)` returns `true` for it. Empty by
 * design, the same way [TileEvent] is: `RadioButton`'s mutual-exclusion mechanism is the built-in
 * example — its own group event carries a `groupId`, and each `RadioButtonTileHolder` compares that
 * against its own `groupId` to decide whether to clear its `selected` state.
 */
interface TileGroupEvent