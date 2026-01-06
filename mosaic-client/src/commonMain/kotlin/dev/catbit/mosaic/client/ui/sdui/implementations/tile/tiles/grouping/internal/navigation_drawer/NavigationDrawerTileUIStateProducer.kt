package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.navigation_drawer

import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.GroupingTileUIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.updater.UIStateProducerUpdater
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIStateProducer

class NavigationDrawerTileUIStateProducer(
    override val id: String,
    override var visibility: TileUIState.Visibility,
    override val style: StyleUIStateProducer,
    override val tiles: MutableList<TileUIStateProducer<*>>,
    override val updater: UIStateProducerUpdater
) : GroupingTileUIStateProducer<NavigationDrawerTileUIState>() {

    override fun update(updateData: Map<String, Any?>) = Unit

    override fun produce() = NavigationDrawerTileUIState(
        id = id,
        style = style.state,
        visibility = visibility,
        tiles = tiles.map { it.state }
    )
}