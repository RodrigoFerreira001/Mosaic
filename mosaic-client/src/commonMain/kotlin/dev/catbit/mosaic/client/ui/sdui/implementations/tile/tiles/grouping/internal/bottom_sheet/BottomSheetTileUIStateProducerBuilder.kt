package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.bottom_sheet

import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.UIStateProducerBuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.tile.GroupingTileUIStateProducerBuilder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducer

internal object BottomSheetTileUIStateProducerBuilder :
    GroupingTileUIStateProducerBuilder<BottomSheetTileModel, BottomSheetTileUIStateProducer> {

    override fun UIStateProducerBuilderScope.buildTile(data: BottomSheetTileModel) = with(data) {
        BottomSheetTileUIStateProducer(
            id = id,
            visibility = visibility.mapTo(),
            style = buildProducer(style),
            tiles = tiles.map { buildProducer<TileUIStateProducer<*>>(it) }.toMutableList(),
            updater = { it },
            isCancellable = isCancellable
        )
    }
}