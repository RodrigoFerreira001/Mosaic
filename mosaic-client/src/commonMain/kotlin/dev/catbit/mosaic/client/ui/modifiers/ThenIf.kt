package dev.catbit.mosaic.client.ui.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Conditionally applies one of two `@Composable` modifier steps — the branch-and-chain idiom behind
 * `Modifier.styledWith`'s `combinedClickable` step, applied only when `onClick`/`onLongClick` is
 * non-null.
 *
 * @param condition which branch to take.
 * @param ifFalse builds the step to apply when [condition] is `false`. Leaves the chain untouched
 * (returns the receiver as-is) when `null`, the default.
 * @param ifTrue builds the step to apply when [condition] is `true`.
 * @return the receiver with the chosen step appended.
 */
@Composable
fun Modifier.thenIf(
    condition: Boolean,
    ifFalse: (@Composable Modifier.() -> Modifier)? = null,
    ifTrue: @Composable Modifier.() -> Modifier
): Modifier {
    return if (condition) this.then(Modifier.ifTrue())
    else ifFalse?.let { this.then(Modifier.it()) } ?: this
}
