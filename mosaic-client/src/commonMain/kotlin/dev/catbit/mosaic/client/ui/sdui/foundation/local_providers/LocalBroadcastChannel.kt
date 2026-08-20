package dev.catbit.mosaic.client.ui.sdui.foundation.local_providers

import androidx.compose.runtime.staticCompositionLocalOf
import dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast.ScreenTilesBroadcastChannel

/**
 * Carries the current screen's [ScreenTilesBroadcastChannel] down the Compose tree — provided once
 * per screen by `MosaicScreen`, read by `TileSchema.observeScreenTileBroadcastChannel`
 * (`extensions/SharedFlowExtensions.kt`). Reading it outside a Mosaic screen throws, which is why
 * every tile renders inside `MosaicScreen`/`MosaicApplication`.
 */
val LocalScreenTilesBroadcastChannel = staticCompositionLocalOf<ScreenTilesBroadcastChannel> {
    error("No BroadcastChannel was provided")
}