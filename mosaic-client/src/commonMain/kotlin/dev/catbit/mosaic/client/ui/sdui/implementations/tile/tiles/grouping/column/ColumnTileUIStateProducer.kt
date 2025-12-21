package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIStateProducer
import dev.catbit.mosaic.client.extensions.valueIfPresent
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.GroupingTileUIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.updater.UIStateProducerUpdater
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState

class ColumnTileUIStateProducer(
    private var arrangement: Arrangement.Vertical,
    private var alignment: Alignment.Horizontal,
    override val id: String,
    override var visibility: TileUIState.Visibility,
    override val style: StyleUIStateProducer,
    override val tiles: MutableList<TileUIStateProducer<*>>,
    override val updater: UIStateProducerUpdater
) : GroupingTileUIStateProducer<ColumnTileUIState>() {

    override fun shouldProduce() = shouldProduceWithLastState { lastState ->
        arrangement != lastState.arrangement
                || alignment != lastState.alignment
                || super.shouldProduce()
    }

    override fun update(updateData: Map<String, Any?>) {
        with(updater.getUpdatedData(updateData)) {
            valueIfPresent<Arrangement.Vertical>(::arrangement.name) { arrangement = it }
            valueIfPresent<Alignment.Horizontal>(::alignment.name) { alignment = it }
            valueIfPresent<TileUIState.Visibility>(::visibility.name) { visibility = it }
            valueIfPresent<Map<String, Any?>>(::style.name) { style.update(it) }
        }
    }

    override fun produce() = ColumnTileUIState(
        id = id,
        style = style.state,
        visibility = visibility,
        tiles = tiles.map { it.state },
        arrangement = arrangement,
        alignment = alignment,
    )
}