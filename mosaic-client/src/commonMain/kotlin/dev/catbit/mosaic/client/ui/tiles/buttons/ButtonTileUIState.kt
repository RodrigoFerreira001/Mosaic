package dev.catbit.mosaic.client.ui.tiles.buttons

import androidx.compose.runtime.Stable
import dev.catbit.mosaic.client.ui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.foundation.state.tile.style.StyleUIState
import dev.catbit.mosaic.client.ui.foundation.state_producer.tile.TileUIStateProducer
import dev.catbit.mosaic.client.ui.foundation.state_producer.tile.style.StyleUIStateProducer

@Stable
data class ButtonTileUIState(
    val text: String,
    override val id: String,
    override val style: StyleUIState,
    override val visibility: TileUIState.Visibility,
) : TileUIState

class ButtonTileUIStateProducer(
    private var text: String,
    override val id: String,
    override var visibility: TileUIState.Visibility,
    override val style: StyleUIStateProducer
) : TileUIStateProducer<ButtonTileUIState>() {

    override fun shouldProduce() = lastState?.let { lastValidState ->
        text != lastValidState.text || visibility != lastValidState.visibility
    } ?: true

    override fun produce() = ButtonTileUIState(
        text = text,
        id = id,
        style = style.state,
        visibility = visibility,
    )
}