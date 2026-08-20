package dev.catbit.mosaic.client.ui.sdui.foundation.events

/**
 * Marker interface for a tile's own local, synchronous state mutation — the payload of
 * [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope.dispatchEvent],
 * handled by that tile's own `TileHolder.onTileEvent` override. Empty by design: every tile that has
 * local state defines its own `sealed interface XTileEvents : TileEvent` with one `data class`/
 * `data object` per distinct mutation (see `CheckboxTileEvents.OnCheckChanged` for the canonical
 * example) — there's nothing to share across tiles beyond "this is a `TileEvent`."
 */
interface TileEvent