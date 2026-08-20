package dev.catbit.mosaic.client.ui.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * `Modifier.then(...)` accepting a `@Composable` builder block instead of a plain `Modifier` value —
 * lets a modifier chain step read `@Composable` ambient state (a `CompositionLocal`, `remember`, a
 * theme value) inline, without breaking the chain into a separate `val` first. Used throughout
 * `Modifier.styledWith`/`Modifier.size` for exactly that reason — most of their individual steps
 * depend on `LocalDensity`/`CompositionLocal`s only available inside a `@Composable` context.
 *
 * @param block builds the next `Modifier` step, with `this` receiver starting from a fresh
 * `Modifier` (not the modifier chain so far) — chain continuation is handled by the outer `.then(...)`
 * call, not by [block] itself.
 */
@Composable
infix fun Modifier.then(block: @Composable Modifier.() -> Modifier): Modifier {
    return this.then(Modifier.block())
}