package dev.catbit.mosaic.client.application

import androidx.compose.runtime.Composable

@Composable
actual fun PlatformDecorator(
    content: @Composable (() -> Unit)
) {
    content()
}