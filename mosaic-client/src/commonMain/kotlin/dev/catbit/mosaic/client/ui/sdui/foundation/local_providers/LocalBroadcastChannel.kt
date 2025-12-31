package dev.catbit.mosaic.client.ui.sdui.foundation.local_providers

import androidx.compose.runtime.staticCompositionLocalOf
import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastChannel

val LocalBroadcastChannel = staticCompositionLocalOf<BroadcastChannel> {
    error("No BroadcastChannel was provided")
}