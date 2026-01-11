package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.dialog

import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.GroupingTileUIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.updater.UIStateProducerUpdater
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIStateProducer

class DialogTileUIStateProducer(
    override val id: String,
    override var visibility: TileUIState.Visibility,
    override val style: StyleUIStateProducer,
    override val tiles: MutableList<TileUIStateProducer<*>>,
    override val updater: UIStateProducerUpdater
) : GroupingTileUIStateProducer<DialogTileUIState>() {

    override fun update(updateData: Map<String, Any?>) = Unit

    override fun produce() = DialogTileUIState(
        id = id,
        style = style.state,
        visibility = visibility,
        tiles = tiles.map { it.state }
    )
}