package dev.catbit.mosaic.client.ui.foundation.state_producer_builder.tile.style

import dev.catbit.mosaic.client.ui.foundation.state_producer.tile.style.SizeUIStateProducer
import dev.catbit.mosaic.client.ui.foundation.state_producer_builder.UIStateProducerBuilder
import dev.catbit.mosaic.client.ui.foundation.state_producer_builder.UIStateProducerBuilderScope
import dev.catbit.mosaic.core.data.tile.style.SizeModel

class SizeUIStateProducerBuilder : UIStateProducerBuilder<SizeModel, SizeUIStateProducer> {

    override fun canBuild(data: Any) = data is SizeModel

    override fun UIStateProducerBuilderScope.build(data: SizeModel) = SizeUIStateProducer(
        width = data.width.mapTo(),
        height = data.height.mapTo(),
    )
}