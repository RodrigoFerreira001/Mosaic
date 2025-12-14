package dev.catbit.mosaic.client.ui.foundation.state.producer.tile

import dev.catbit.mosaic.client.ui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.foundation.state.producer.UIStateProducer
import dev.catbit.mosaic.client.ui.base_implementations.tile.style.StyleUIStateProducer

abstract class TileUIStateProducer<out T : TileUIState> : UIStateProducer<T>() {
    abstract val id: String
    protected abstract var visibility: TileUIState.Visibility
    protected abstract val style: StyleUIStateProducer
    protected fun onEvent(event: UIEvent) = Unit
}