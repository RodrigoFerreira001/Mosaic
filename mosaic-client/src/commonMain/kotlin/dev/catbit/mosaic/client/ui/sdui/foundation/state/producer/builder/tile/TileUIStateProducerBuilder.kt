package dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.tile

import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.UIStateProducerBuilder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.UIStateProducerBuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducer
import dev.catbit.mosaic.core.data.tile.TileModel

interface TileUIStateProducerBuilder<out D : TileModel, out T : TileUIStateProducer<*>> : UIStateProducerBuilder<D, T> {

    override fun UIStateProducerBuilderScope.build(data: @UnsafeVariance D): T {
        data.events?.let { registerEvents(data.id, it) }
        return buildTile(data)
    }

    fun UIStateProducerBuilderScope.buildTile(data: @UnsafeVariance D): T
}