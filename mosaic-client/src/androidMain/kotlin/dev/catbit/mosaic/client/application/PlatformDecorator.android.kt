package dev.catbit.mosaic.client.application

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dev.catbit.mosaic.client.context.AndroidContextWrapper

@Composable
actual fun PlatformDecorator(
    content: @Composable (() -> Unit)
) {
    AndroidContextWrapper.setContext(LocalContext.current)
    content()
}