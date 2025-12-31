package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.text

import dev.catbit.mosaic.client.extensions.extractAndPutIfPresent
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.UIStateProducerBuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.tile.TileUIStateProducerBuilder
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.tiles.text.TextTileModel

object TextTileUIStateProducerBuilder :
    TileUIStateProducerBuilder<TextTileModel, TextTileUIStateProducer> {

    override fun UIStateProducerBuilderScope.buildTile(
        data: TextTileModel
    ) = with(data) {
        TextTileUIStateProducer(
            text = text,
            id = id,
            visibility = visibility.mapTo(),
            style = buildProducer(style),
            updater = { updateData ->
                mutableMapOf<String, Any?>().apply {
                    extractAndPutIfPresent(TextTileModel::text.name, updateData) { decode<String>(it) }
                    extractAndPutIfPresent(TextTileModel::style.name, updateData)
                    extractAndPutIfPresent(TextTileModel::visibility.name, updateData) {
                        decode<TileModel.Visibility>(it).mapTo()
                    }
                }
            }
        )
    }
}