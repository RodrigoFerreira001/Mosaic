package dev.catbit.mosaic.client.ui.sdui.foundation.local_providers

import androidx.compose.runtime.staticCompositionLocalOf
import dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast.ScreenTilesBroadcastChannel

val LocalScreenTilesBroadcastChannel = staticCompositionLocalOf<ScreenTilesBroadcastChannel> {
    error("No BroadcastChannel was provided")
}