package dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile

import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.UIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState

abstract class TileUIStateProducer<out T : TileUIState> : UIStateProducer<T>() {
    // TODO criar alguma abstração para validar o state base do TileUIState shouldProduce
    abstract val id: String
    protected abstract var visibility: TileUIState.Visibility
    protected abstract val style: StyleUIStateProducer
    open fun TileUIStateProducerScope.onEvent(event: TileEvent) = Unit
}