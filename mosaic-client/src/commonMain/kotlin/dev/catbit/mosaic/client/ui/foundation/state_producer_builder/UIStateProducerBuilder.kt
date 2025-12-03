package dev.catbit.mosaic.client.ui.foundation.state_producer_builder

import dev.catbit.mosaic.client.ui.foundation.state_producer.UIStateProducer

interface UIStateProducerBuilder<out D: Any, out B: UIStateProducer<*>> {
    fun canBuild(data: Any): Boolean
    fun UIStateProducerBuilderScope.build(data: @UnsafeVariance D): B
}