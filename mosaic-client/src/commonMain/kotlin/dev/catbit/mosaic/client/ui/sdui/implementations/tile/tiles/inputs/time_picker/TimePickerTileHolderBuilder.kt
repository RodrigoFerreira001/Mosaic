package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.time_picker

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TimePickerTileSchema

object TimePickerTileHolderBuilder : TileHolderBuilder<TimePickerTileSchema, TimePickerTileHolder> {

    override fun BuilderScope.build(
        tileModel: TimePickerTileSchema
    ) = with(tileModel) {
        TimePickerTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders(),
        )
    }
}
