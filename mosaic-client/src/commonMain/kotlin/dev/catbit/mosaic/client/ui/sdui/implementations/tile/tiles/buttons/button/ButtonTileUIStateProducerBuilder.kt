package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.button

import dev.catbit.mosaic.client.extensions.extractAndPutIfPresent
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.UIStateProducerBuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.tile.TileUIStateProducerBuilder
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.tiles.buttons.ButtonTileModel

object ButtonTileUIStateProducerBuilder :
    TileUIStateProducerBuilder<ButtonTileModel, ButtonTileUIStateProducer> {

    override fun UIStateProducerBuilderScope.buildTile(
        data: ButtonTileModel
    ) = with(data) {
        ButtonTileUIStateProducer(
            text = text,
            loading = false,
            id = id,
            visibility = visibility.mapTo(),
            style = buildProducer(style),
            updater = { updateData ->
                mutableMapOf<String, Any?>().apply {
                    extractAndPutIfPresent(ButtonTileModel::text.name, updateData) { decode<String>(it) }
                    extractAndPutIfPresent(ButtonTileModel::loading.name, updateData) { decode<Boolean>(it) }
                    extractAndPutIfPresent(ButtonTileModel::style.name, updateData)
                    extractAndPutIfPresent(ButtonTileModel::visibility.name, updateData) {
                        decode<TileModel.Visibility>(it).mapTo()
                    }
                }
            }
        )
    }
}