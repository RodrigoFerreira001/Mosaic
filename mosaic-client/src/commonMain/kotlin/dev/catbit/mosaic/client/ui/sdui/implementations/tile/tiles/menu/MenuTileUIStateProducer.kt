package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu

import dev.catbit.mosaic.client.extensions.valueIfPresent
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.GroupingTileUIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducerScope
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.updater.UIStateProducerUpdater
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIStateProducer

class MenuTileUIStateProducer(
    private var expanded: Boolean = false,
    private var items: List<MenuTileUIState.MenuItem>,
    override val id: String,
    override var visibility: TileUIState.Visibility,
    override val style: StyleUIStateProducer,
    override val updater: UIStateProducerUpdater,
    override val tiles: MutableList<TileUIStateProducer<*>>,
) : GroupingTileUIStateProducer<MenuTileUIState>() {

    override fun update(updateData: Map<String, Any?>) {
        with(updater.getUpdatedData(updateData)) {
            valueIfPresent<List<MenuTileUIState.MenuItem>>(::items.name) { items = it }
            valueIfPresent<TileUIState.Visibility>(::visibility.name) { visibility = it }
            valueIfPresent<Map<String, Any?>>(::style.name) { style.update(it) }
        }
    }

    override fun shouldProduce() = shouldProduceWithLastState { state ->
        state.expanded != expanded
                || state.items != items
                || style.shouldProduce()
                || super.shouldProduce()
    }

    override fun produce() = MenuTileUIState(
        id = id,
        style = style.state,
        tiles = tiles.map { it.state },
        visibility = visibility,
        items = items,
        expanded = expanded,
    )

    override fun TileUIStateProducerScope.onEvent(event: TileEvent) {
        when (event) {
            is MenuTileEvent.OnToggleMenu -> {
                expanded = !expanded
                updateState()
            }
        }
    }
}