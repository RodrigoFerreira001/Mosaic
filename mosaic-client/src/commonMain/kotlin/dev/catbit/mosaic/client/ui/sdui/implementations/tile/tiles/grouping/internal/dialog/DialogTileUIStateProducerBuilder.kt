package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.dialog

import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.UIStateProducerBuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.tile.GroupingTileUIStateProducerBuilder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducer

internal object DialogTileUIStateProducerBuilder :
    GroupingTileUIStateProducerBuilder<DialogTileModel, DialogTileUIStateProducer> {

    override fun UIStateProducerBuilderScope.buildTile(data: DialogTileModel) = with(data) {
        DialogTileUIStateProducer(
            id = id,
            visibility = visibility.mapTo(),
            style = buildProducer(style),
            tiles = tiles.map { buildProducer<TileUIStateProducer<*>>(it) }.toMutableList(),
            updater = { it },
            isCancellable = isCancellable,
            usePlatformDefaultWidth = usePlatformDefaultWidth
        )
    }
}