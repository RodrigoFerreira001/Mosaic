package dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.tile

import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.GroupingTileUIStateProducer
import dev.catbit.mosaic.core.data.tile.TileModel

interface GroupingTileUIStateProducerBuilder<out D : TileModel, out T : GroupingTileUIStateProducer<*>> :
    TileUIStateProducerBuilder<D, T>