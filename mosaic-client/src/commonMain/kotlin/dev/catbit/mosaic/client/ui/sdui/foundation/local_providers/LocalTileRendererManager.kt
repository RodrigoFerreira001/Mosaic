package dev.catbit.mosaic.client.ui.sdui.foundation.local_providers

import androidx.compose.runtime.compositionLocalOf
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRendererManager

val LocalTileRendererManager = compositionLocalOf<TileRendererManager> {
    error("No TileRendererManager was provided")
}