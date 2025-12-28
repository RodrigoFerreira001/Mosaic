package dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile

import dev.catbit.mosaic.client.ui.sdui.foundation.state.manager.TilesEditor
import dev.catbit.mosaic.client.ui.sdui.foundation.state.manager.TilesStateUpdater

class TileUIStateProducerScope(
    private val tilesEditor: TilesEditor,
    private val tilesStateUpdater: TilesStateUpdater
) {
    fun updateState() {
        tilesStateUpdater.updateState()
    }
}