package dev.catbit.mosaic.client.application

import androidx.compose.runtime.Composable

@Composable
internal actual fun PlatformWrapper(
    content: @Composable (() -> Unit)
) {
    content()
}