package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.dropdown_list

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DropdownListTileSchema

object DropdownListTileHolderBuilder : TileHolderBuilder<DropdownListTileSchema, DropdownListTileHolder> {

    override fun BuilderScope.build(
        tileModel: DropdownListTileSchema
    ) = with(tileModel) {
        DropdownListTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders(),
        )
    }
}
