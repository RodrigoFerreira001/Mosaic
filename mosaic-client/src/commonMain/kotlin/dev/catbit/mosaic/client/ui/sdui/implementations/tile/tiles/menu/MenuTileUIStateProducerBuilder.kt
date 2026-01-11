package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu

import dev.catbit.mosaic.client.extensions.extractAndPutIfPresent
import dev.catbit.mosaic.client.extensions.extractAndPutIfPresentOrNull
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.UIStateProducerBuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.tile.GroupingTileUIStateProducerBuilder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducer
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.tiles.menu.MenuTileModel

object MenuTileUIStateProducerBuilder : GroupingTileUIStateProducerBuilder<MenuTileModel, MenuTileUIStateProducer> {

    override fun UIStateProducerBuilderScope.buildTile(
        data: MenuTileModel
    ) = with(data) {
        MenuTileUIStateProducer(
            items = toUIState(items),
            id = id,
            visibility = visibility.mapTo(),
            style = buildProducer(style),
            tiles = tiles.map { buildProducer<TileUIStateProducer<*>>(it) }.toMutableList(),
            updater = { updateData ->
                mutableMapOf<String, Any?>().apply {
                    extractAndPutIfPresentOrNull(MenuTileModel::items.name, updateData) {
                        decodeOrNull<List<MenuTileModel.MenuItem>>(it)?.let { items ->
                            toUIState(items)
                        }
                    }
                    extractAndPutIfPresent(MenuTileModel::visibility.name, updateData) {
                        decode<TileModel.Visibility>(it).mapTo()
                    }
                    extractAndPutIfPresent(MenuTileModel::style.name, updateData)
                }
            }
        )
    }

    private fun UIStateProducerBuilderScope.toUIState(
        items: List<MenuTileModel.MenuItem>
    ): List<MenuTileUIState.MenuItem> = items.map { item ->
        MenuTileUIState.MenuItem(
            id = item.id,
            label = item.label,
            leadingIcon = item.leadingIcon?.mapTo(),
            trailingIcon = item.trailingIcon?.mapTo(),
        )
    }
}