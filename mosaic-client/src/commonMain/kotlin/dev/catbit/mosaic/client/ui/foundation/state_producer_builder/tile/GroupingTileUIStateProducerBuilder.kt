package dev.catbit.mosaic.client.ui.foundation.state_producer_builder.tile

import dev.catbit.mosaic.client.ui.foundation.state_producer.tile.GroupingTileUIStateProducer
import dev.catbit.mosaic.core.data.tile.TileModel

interface GroupingTileUIStateProducerBuilder<out D : TileModel, out T : GroupingTileUIStateProducer<*>> :
    TileUIStateProducerBuilder<D, T>