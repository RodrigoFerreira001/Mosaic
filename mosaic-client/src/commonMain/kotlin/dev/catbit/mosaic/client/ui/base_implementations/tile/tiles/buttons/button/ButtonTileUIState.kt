package dev.catbit.mosaic.client.ui.base_implementations.tile.tiles.buttons.button

import androidx.compose.runtime.Stable
import dev.catbit.mosaic.client.ui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.base_implementations.tile.style.StyleUIState
import dev.catbit.mosaic.client.ui.foundation.state.producer.tile.TileUIStateProducer
import dev.catbit.mosaic.client.ui.base_implementations.tile.style.StyleUIStateProducer

@Stable
data class ButtonTileUIState(
    val text: String,
    val loading: Boolean,
    override val id: String,
    override val style: StyleUIState,
    override val visibility: TileUIState.Visibility,
) : TileUIState