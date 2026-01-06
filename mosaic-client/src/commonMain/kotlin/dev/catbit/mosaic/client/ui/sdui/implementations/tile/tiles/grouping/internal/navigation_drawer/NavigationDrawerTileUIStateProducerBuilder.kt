package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.navigation_drawer

import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.UIStateProducerBuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.tile.GroupingTileUIStateProducerBuilder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducer

internal object NavigationDrawerTileUIStateProducerBuilder :
    GroupingTileUIStateProducerBuilder<NavigationDrawerTileModel, NavigationDrawerTileUIStateProducer> {

    override fun UIStateProducerBuilderScope.buildTile(data: NavigationDrawerTileModel) = with(data) {
        NavigationDrawerTileUIStateProducer(
            id = id,
            visibility = visibility.mapTo(),
            style = buildProducer(style),
            tiles = tiles.map { buildProducer<TileUIStateProducer<*>>(it) }.toMutableList(),
            updater = { it },
        )
    }
}