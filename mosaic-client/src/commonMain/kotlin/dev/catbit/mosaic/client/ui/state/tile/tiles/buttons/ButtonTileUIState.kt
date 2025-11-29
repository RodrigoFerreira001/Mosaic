package dev.catbit.mosaic.client.ui.state.tile.tiles.buttons

import androidx.compose.runtime.Stable
import dev.catbit.mosaic.client.ui.state.builder.TileUIStateBuilder
import dev.catbit.mosaic.client.ui.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.state.style.StyleUIState
import dev.catbit.mosaic.client.ui.state.style.StyleUIStateBuilder

@Stable
data class ButtonTileUIState(
    val text: String,
    override val id: String,
    override val style: StyleUIState,
    override val visibility: TileUIState.Visibility,
) : TileUIState

class ButtonTileUIStateBuilder(
    private var text: String,
    override val id: String,
    override var visibility: TileUIState.Visibility,
    override val style: StyleUIStateBuilder
) : TileUIStateBuilder<ButtonTileUIState>() {

    override fun shouldRebuild() = lastState?.let { lastValidState ->
        text != lastValidState.text || visibility != lastValidState.visibility
    } ?: true

    override fun build() = ButtonTileUIState(
        text = text,
        id = id,
        style = style.state,
        visibility = visibility,
    )
}