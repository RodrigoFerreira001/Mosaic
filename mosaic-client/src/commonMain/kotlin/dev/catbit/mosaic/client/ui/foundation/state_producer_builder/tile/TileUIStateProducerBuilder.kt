package dev.catbit.mosaic.client.ui.foundation.state_producer_builder.tile

import dev.catbit.mosaic.client.ui.foundation.state_producer.tile.TileUIStateProducer
import dev.catbit.mosaic.client.ui.foundation.state_producer_builder.UIStateProducerBuilder
import dev.catbit.mosaic.core.data.tile.TileModel

interface TileUIStateProducerBuilder<out D : TileModel, out T : TileUIStateProducer<*>> : UIStateProducerBuilder<D, T>