package dev.catbit.mosaic.client.ui.sdui.foundation.local_providers

import androidx.compose.runtime.compositionLocalOf
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRendererManager

/**
 * Carries the app-wide [TileRendererManager] down the tree — provided once, near the root, so
 * `TileRenderingScope.RenderChild`/`RenderChildren` can reach it without either being handed the
 * manager as an explicit parameter (which would need to thread through every single container tile's
 * `Render` implementation). Reading it outside a Mosaic screen throws, which is why every tile
 * renders inside `MosaicApplication`.
 */
val LocalTileRendererManager = compositionLocalOf<TileRendererManager> {
    error("No TileRendererManager was provided")
}