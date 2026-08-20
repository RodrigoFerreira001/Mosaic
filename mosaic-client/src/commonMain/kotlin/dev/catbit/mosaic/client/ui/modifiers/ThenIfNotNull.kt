package dev.catbit.mosaic.client.ui.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Applies a `@Composable` modifier step only when [value] is non-null, passing the unwrapped value
 * into [block] — the idiom behind every optional field in `Modifier.styledWith` (`windowInsets`,
 * `margin`, `clip`, `background`, `border`, `padding` are each applied this way, since a tile's
 * `StyleSchema` leaves most of them `null` by default).
 *
 * @param value the optional value to check.
 * @param block builds the step to apply, receiving [value] unwrapped. Not called at all when [value]
 * is `null` — the receiver is returned unchanged in that case.
 * @return the receiver with the step appended, or unchanged if [value] is `null`.
 */
@Composable
fun <T> Modifier.thenIfNotNull(
    value: T?,
    block: @Composable Modifier.(T) -> Modifier
): Modifier {
    return value?.let { this.then(Modifier.block(it)) } ?: this
}
