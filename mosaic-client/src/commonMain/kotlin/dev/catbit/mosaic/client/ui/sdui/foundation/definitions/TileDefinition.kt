package dev.catbit.mosaic.client.ui.sdui.foundation.definitions

import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.tile.TileUIStateProducerBuilder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderer
import dev.catbit.mosaic.core.data.tile.TileModel
import kotlin.reflect.KClass

interface TileDefinition <Model: TileModel, UIState: TileUIState> {
    val tileModelClass: KClass<Model>
    val tileRenderer: TileRenderer<UIState>
    val tileUIStateProducerBuilder: TileUIStateProducerBuilder<Model, TileUIStateProducer<UIState>>
}