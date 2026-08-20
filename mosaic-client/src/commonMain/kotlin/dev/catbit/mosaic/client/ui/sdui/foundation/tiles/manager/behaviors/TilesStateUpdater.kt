package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors

/**
 * Single-method surface that lets a `TilesManager` (and its `ScreenTileHolder`) tell whoever owns the
 * screen's UI state that the live tile tree changed and needs to be re-read and recomposed.
 */
interface TilesStateUpdater {
    /**
     * Re-reads the current root tile schema (via `get()` on the screen's holder) and pushes it to
     * the screen's own state — called after every mutating [TilesEditor]/[TilesOverlaysEditor]/
     * [TilesEventDispatcher] operation succeeds, so its effect actually reaches Compose.
     */
    fun updateState()
}