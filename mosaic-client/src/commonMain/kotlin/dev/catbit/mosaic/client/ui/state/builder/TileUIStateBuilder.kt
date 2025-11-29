package dev.catbit.mosaic.client.ui.state.builder

import dev.catbit.mosaic.client.ui.state.style.StyleUIStateBuilder
import dev.catbit.mosaic.client.ui.state.tile.TileUIState

abstract class TileUIStateBuilder<out T : TileUIState> : UIStateBuilder<T>() {
    abstract val id: String
    protected abstract var visibility: TileUIState.Visibility
    protected abstract val style: StyleUIStateBuilder
}