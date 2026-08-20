package dev.catbit.mosaic.client.ui.sdui.foundation.local_providers

import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.runtime.compositionLocalOf

/**
 * Carries the innermost `FlowRow`'s Compose `FlowRowScope` down the tree, or `null` when no ancestor
 * `FlowRow` has provided one — read by `Modifier.size()` to resolve a child's horizontal `Weight`
 * (via `flowRowScope.weight`) and vertical `FillRow` sizing behavior. Provided by
 * `FlowRowTileRenderer`, which also resets `LocalRowScope`/`LocalLazyItemScope` to `null`; reset back
 * to `null` in turn by `RowTileRenderer`/`LazyRowTileRenderer`.
 */
val LocalFlowRowScope = compositionLocalOf<FlowRowScope?> { null }
