package dev.catbit.mosaic.client.ui.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> Modifier.thenIfNotNull(
    value: T?,
    block: @Composable Modifier.(T) -> Modifier
): Modifier {
    return value?.let { this then(block(it)) } ?: this
}
