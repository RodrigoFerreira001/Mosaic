package dev.catbit.mosaic.client.ui.foundation.state_producer_builder.tile.style

import dev.catbit.mosaic.client.ui.foundation.state_producer.tile.style.StyleUIStateProducer
import dev.catbit.mosaic.client.ui.foundation.state_producer_builder.UIStateProducerBuilder
import dev.catbit.mosaic.client.ui.foundation.state_producer_builder.UIStateProducerBuilderScope
import dev.catbit.mosaic.core.data.tile.style.StyleModel

class StyleUIStateProducerBuilder : UIStateProducerBuilder<StyleModel, StyleUIStateProducer> {

    override fun canBuild(data: Any) = data is StyleModel

    override fun UIStateProducerBuilderScope.build(data: StyleModel) = with(data) {
        StyleUIStateProducer(
            size = buildProducer(size),
            margin = margin?.mapTo(),
            padding = padding?.mapTo(),
            background = background?.mapTo(),
            border = border?.let { buildProducer(it) },
            clip = clip?.let { buildProducer(it) },
            windowInsets = windowInsets?.mapTo(),
        )
    }
}
