package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field

import androidx.compose.runtime.Stable
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIState

@Stable
data class TextFieldTileUIState(
    override val id: String,
    override val style: StyleUIState,
    override val visibility: TileUIState.Visibility,
    val value: String
) : TileUIState