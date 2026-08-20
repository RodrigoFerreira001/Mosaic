package dev.catbit.mosaic.client.exceptions

/**
 * Thrown when a tile is looked up by id and none matches — the failure behind every
 * [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEditor] method that
 * targets a tile/`groupingTileId` by id, and `TilesEventDispatcher.onEvent`/`onGroupEvent`.
 */
class TileNotFoundException(
    override val message: String?
) : Throwable()
