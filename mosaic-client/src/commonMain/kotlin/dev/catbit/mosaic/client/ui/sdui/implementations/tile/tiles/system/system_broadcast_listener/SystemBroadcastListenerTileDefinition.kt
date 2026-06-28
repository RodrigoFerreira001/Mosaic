package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.system.system_broadcast_listener

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.system.SystemBroadcastListenerTileSchema

object SystemBroadcastListenerTileDefinition : TileDefinition<SystemBroadcastListenerTileSchema> {
    override val tileSchemaClass = SystemBroadcastListenerTileSchema::class
    override val tileRenderer = SystemBroadcastListenerTileRenderer
    override val tileHolderBuilder = SystemBroadcastListenerTileHolderBuilder
}
