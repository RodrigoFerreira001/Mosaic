package dev.catbit.mosaic.client.ui.sdui.implementations.tile.style

import androidx.compose.foundation.layout.PaddingValues
import dev.catbit.mosaic.client.extensions.extractAndPutIfPresent
import dev.catbit.mosaic.client.extensions.extractAndPutIfPresentOrNull
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.UIStateProducerBuilder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.UIStateProducerBuilderScope
import dev.catbit.mosaic.core.data.color.ColorModel
import dev.catbit.mosaic.core.data.tile.style.BorderModel
import dev.catbit.mosaic.core.data.tile.style.ClipModel
import dev.catbit.mosaic.core.data.tile.style.MarginModel
import dev.catbit.mosaic.core.data.tile.style.SizeModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import dev.catbit.mosaic.core.data.tile.style.WindowInsetsModel

object StyleUIStateProducerBuilder :
    UIStateProducerBuilder<StyleModel, StyleUIStateProducer> {

    override fun UIStateProducerBuilderScope.build(data: StyleModel) = with(data) {
        StyleUIStateProducer(
            size = size.mapTo(),
            margin = margin?.mapTo(),
            padding = padding?.mapTo(),
            background = background?.mapTo(),
            border = border?.mapTo(),
            clip = clip?.mapTo(),
            windowInsets = windowInsets?.mapTo(),
            updater = { updateData ->
                mutableMapOf<String, Any?>().apply {
                    extractAndPutIfPresent(StyleModel::size.name, updateData) {
                        decode<SizeModel>(it).mapTo()
                    }
                    extractAndPutIfPresentOrNull(StyleModel::margin.name, updateData) {
                        decodeOrNull<MarginModel>(it)?.mapTo()
                    }
                    extractAndPutIfPresentOrNull(StyleModel::padding.name, updateData) {
                        decodeOrNull<PaddingValues>(it)?.mapTo()
                    }
                    extractAndPutIfPresentOrNull(StyleModel::background.name, updateData) {
                        decodeOrNull<ColorModel>(it)?.mapTo()
                    }
                    extractAndPutIfPresentOrNull(StyleModel::border.name, updateData) {
                        decodeOrNull<BorderModel>(it)?.mapTo()
                    }
                    extractAndPutIfPresentOrNull(StyleModel::clip.name, updateData) {
                        decodeOrNull<ClipModel>(it)?.mapTo()
                    }
                    extractAndPutIfPresentOrNull(StyleModel::windowInsets.name, updateData) {
                        decodeOrNull<WindowInsetsModel>(it)?.mapTo()
                    }
                }
            }
        )
    }
}