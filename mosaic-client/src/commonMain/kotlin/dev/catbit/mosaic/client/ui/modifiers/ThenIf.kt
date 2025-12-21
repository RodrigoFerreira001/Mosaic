package dev.catbit.mosaic.client.ui.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.thenIf(
    condition: Boolean,
    ifFalse: (@Composable Modifier.() -> Modifier)? = null,
    ifTrue: @Composable Modifier.() -> Modifier
): Modifier {
    return if (condition) this then(ifTrue()) else ifFalse?.let { it() } ?: this
}
