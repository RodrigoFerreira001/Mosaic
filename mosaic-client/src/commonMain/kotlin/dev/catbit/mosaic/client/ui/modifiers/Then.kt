package dev.catbit.mosaic.client.ui.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
infix fun Modifier.then(block: @Composable Modifier.() -> Modifier): Modifier {
    return this then(block())
}