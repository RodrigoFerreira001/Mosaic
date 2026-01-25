package dev.catbit.mosaic.client.ui.sdui.foundation.definitions

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.core.data.tile.TileModel
import kotlin.reflect.KClass

interface TileDefinition <Model: TileModel> {
    val tileModelClass: KClass<Model>
    val tileRenderer: TileRenderer<Model>
    val tileHolderBuilder: TileHolderBuilder<Model, out TileHolder<Model>>
}