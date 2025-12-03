package dev.catbit.mosaic.client.ui.tiles.grouping.row

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import dev.catbit.mosaic.client.ui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.foundation.state_producer.tile.GroupingTileUIStateProducer
import dev.catbit.mosaic.client.ui.foundation.state_producer.tile.TileUIStateProducer
import dev.catbit.mosaic.client.ui.foundation.state_producer.tile.style.StyleUIStateProducer

class ColumnTileUIStateProducer(
    override val id: String,
    override var visibility: TileUIState.Visibility,
    override val style: StyleUIStateProducer,
    override val tiles: MutableList<TileUIStateProducer<*>>,
    private var arrangement: Arrangement.Vertical,
    private var alignment: Alignment.Horizontal
) : GroupingTileUIStateProducer<ColumnTileUIState>() {

    override fun produce() = ColumnTileUIState(
        id = id,
        visibility = visibility,
        style = style.state,
        tiles = tiles,
        arrangement = arrangement,
        alignment = alignment,
    )
}