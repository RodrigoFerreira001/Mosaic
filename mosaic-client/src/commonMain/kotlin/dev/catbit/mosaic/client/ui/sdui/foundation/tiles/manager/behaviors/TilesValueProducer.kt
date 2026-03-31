package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors

interface TilesValueProducer {
    fun getValueWithKey(
        tileId: String,
        key: String
    ): Map<String, Any>?
}