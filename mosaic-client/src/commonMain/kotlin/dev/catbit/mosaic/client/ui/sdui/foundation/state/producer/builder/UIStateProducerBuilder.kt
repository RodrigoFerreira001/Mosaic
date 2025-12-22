package dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder

import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.UIStateProducer

interface UIStateProducerBuilder<out D : Any, out B : UIStateProducer<*>> {
    fun UIStateProducerBuilderScope.build(data: @UnsafeVariance D): B
}