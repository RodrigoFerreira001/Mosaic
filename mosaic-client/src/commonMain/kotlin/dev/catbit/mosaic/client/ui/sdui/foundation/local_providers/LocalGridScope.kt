package dev.catbit.mosaic.client.ui.sdui.foundation.local_providers

import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.GridScope
import androidx.compose.runtime.compositionLocalOf

/**
 * Carries the innermost `Grid`'s Compose `GridScope` down the tree, or `null` when no ancestor
 * `Grid` has provided one — read by `Modifier.size()` to resolve a child's `Span` sizing behavior
 * (row/column span placement). Provided by `GridTileRenderer`; unlike
 * `LocalColumnScope`/`LocalRowScope`/`LocalLazyItemScope`, no other container renderer resets this
 * back to `null`, so it keeps propagating through any descendant subtree until another `Grid`
 * provides a fresh value.
 */
@OptIn(ExperimentalGridApi::class)
val LocalGridScope = compositionLocalOf<GridScope?> { null }
