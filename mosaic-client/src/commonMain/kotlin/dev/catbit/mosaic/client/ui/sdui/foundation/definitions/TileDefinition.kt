package dev.catbit.mosaic.client.ui.sdui.foundation.definitions

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlin.reflect.KClass

interface TileDefinition <Schema: TileSchema> {
    val tileSchemaClass: KClass<Schema>
    val tileRenderer: TileRenderer<Schema>
    val tileHolderBuilder: TileHolderBuilder<Schema, out TileHolder<Schema>>
}