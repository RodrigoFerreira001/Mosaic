package dev.catbit.mosaic.client.ui.foundation.state.producer.builder.tile

import dev.catbit.mosaic.client.ui.foundation.state.producer.tile.TileUIStateProducer
import dev.catbit.mosaic.client.ui.foundation.state.producer.builder.UIStateProducerBuilder
import dev.catbit.mosaic.core.data.tile.TileModel

interface TileUIStateProducerBuilder<out D : TileModel, out T : TileUIStateProducer<*>> : UIStateProducerBuilder<D, T>