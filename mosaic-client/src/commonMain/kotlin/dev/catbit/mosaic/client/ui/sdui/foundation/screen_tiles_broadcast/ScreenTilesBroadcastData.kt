package dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast

/**
 * Base contract for every value published on a screen's own
 * [ScreenTilesBroadcastChannel] — implemented by one `data class` per command kind (scroll-to,
 * overlay open/close, etc.). [tileId] is what
 * `TileSchema.observeScreenTileBroadcastChannel(filterByTileId = true, ...)` compares against a
 * subscriber's own id to decide whether a given command is addressed to it.
 *
 * @property tileId id of the tile this broadcast targets, or `null` for a command with no single
 * addressed tile (e.g. an overlay command, which is addressed by overlay id rather than tile id).
 */
interface ScreenTilesBroadcastData {
    val tileId: String?
}