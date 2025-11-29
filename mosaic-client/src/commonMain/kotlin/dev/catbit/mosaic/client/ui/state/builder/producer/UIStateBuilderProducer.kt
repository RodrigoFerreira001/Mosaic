package dev.catbit.mosaic.client.ui.state.builder.producer

import dev.catbit.mosaic.client.ui.state.builder.UIStateBuilder

interface UIStateBuilderProducer<out D: Any, out B: UIStateBuilder<*>> {
    fun canProduce(data: Any): Boolean
    fun UIStateBuilderProducerScope.produce(data: @UnsafeVariance D): B
}