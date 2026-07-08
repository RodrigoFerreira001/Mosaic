package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.date_picker

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DatePickerTileSchema

object DatePickerTileHolderBuilder : TileHolderBuilder<DatePickerTileSchema, DatePickerTileHolder> {

    override fun BuilderScope.build(
        tileModel: DatePickerTileSchema
    ) = with(tileModel) {
        DatePickerTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders(),
        )
    }
}
