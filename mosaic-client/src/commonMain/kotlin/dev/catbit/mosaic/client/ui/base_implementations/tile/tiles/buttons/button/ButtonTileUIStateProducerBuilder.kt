package dev.catbit.mosaic.client.ui.base_implementations.tile.tiles.buttons.button

import dev.catbit.mosaic.client.ui.extensions.extractAndPutIfPresent
import dev.catbit.mosaic.client.ui.foundation.state.producer.builder.UIStateProducerBuilderScope
import dev.catbit.mosaic.client.ui.foundation.state.producer.builder.tile.TileUIStateProducerBuilder
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.tiles.buttons.ButtonTileModel

class ButtonTileUIStateProducerBuilder :
    TileUIStateProducerBuilder<ButtonTileModel, ButtonTileUIStateProducer> {
    override fun canBuild(data: Any) = data is ButtonTileModel

    override fun UIStateProducerBuilderScope.build(
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