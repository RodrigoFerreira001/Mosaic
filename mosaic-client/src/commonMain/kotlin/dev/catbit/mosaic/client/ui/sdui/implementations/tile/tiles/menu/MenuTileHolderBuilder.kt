package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.menu.MenuTileSchema

object MenuTileHolderBuilder : TileHolderBuilder<MenuTileSchema, MenuTileHolder> {

    override fun BuilderScope.build(
        tileModel: MenuTileSchema
    ) = with(tileModel) {
        MenuTileHolder(
            id = id,
            tile = tileModel,
            events = events?.buildEventHolders(),
            tiles = tiles.buildTileHolders()
        )
    }
}