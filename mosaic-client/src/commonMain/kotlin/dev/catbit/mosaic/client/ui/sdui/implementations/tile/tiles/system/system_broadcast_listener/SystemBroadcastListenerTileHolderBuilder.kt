package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.system.system_broadcast_listener

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.system.SystemBroadcastListenerTileSchema

object SystemBroadcastListenerTileHolderBuilder :
    TileHolderBuilder<SystemBroadcastListenerTileSchema, SystemBroadcastListenerTileHolder> {

    override fun BuilderScope.build(
        tileModel: SystemBroadcastListenerTileSchema
    ) = with(tileModel) {
        SystemBroadcastListenerTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders(),
            tiles = tiles.buildTileHolders()
        )
    }
}
