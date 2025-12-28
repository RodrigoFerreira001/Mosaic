package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field

import dev.catbit.mosaic.client.extensions.extractAndPutIfPresent
import dev.catbit.mosaic.client.extensions.extractAndPutIfPresentOrNull
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.UIStateProducerBuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.tile.TileUIStateProducerBuilder
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.tiles.inputs.TextFieldTileModel

object TextFieldTileUIStateProducerBuilder :
    TileUIStateProducerBuilder<TextFieldTileModel, TextFieldTileUIStateProducer> {
    override fun UIStateProducerBuilderScope.buildTile(
        data: TextFieldTileModel
    ) = with(data) {
        TextFieldTileUIStateProducer(
            value = value.orEmpty(),
            id = id,
            visibility = visibility.mapTo(),
            style = buildProducer(style),
            updater = { updateData ->
                mutableMapOf<String, Any?>().apply {
                    extractAndPutIfPresentOrNull(TextFieldTileModel::value.name, updateData) {
                        decodeOrNull<String>(it)
                    }
                    extractAndPutIfPresent(TextFieldTileModel::visibility.name, updateData) {
                        decode<TileModel.Visibility>(it).mapTo()
                    }
                    extractAndPutIfPresent(TextFieldTileModel::style.name, updateData)
                }
            }
        )
    }
}