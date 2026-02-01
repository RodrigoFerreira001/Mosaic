package dev.catbit.mosaic.client.application

import androidx.compose.runtime.Composable

@Composable
expect fun PlatformDecorator(
    content: @Composable () -> Unit
)