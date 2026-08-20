package dev.catbit.mosaic.client.ui.sdui.foundation.local_providers

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.compositionLocalOf

/**
 * Carries the innermost `Row`'s Compose `RowScope` down the tree, or `null` when no ancestor `Row`
 * has provided one yet — read by `Modifier.size()` to resolve a child's horizontal `Weight` sizing
 * behavior. Provided by `RowTileRenderer`, which also resets `LocalLazyItemScope`/`LocalFlowRowScope`
 * to `null`; reset back to `null` in turn by `FlowRowTileRenderer`/`LazyRowTileRenderer`. Like
 * `LocalColumnScope`, `ColumnTileRenderer` doesn't reset this, so a `Column` nested inside a `Row`
 * still sees the outer `Row`'s scope through it — harmless for `Modifier.size()` itself, since it
 * checks `LocalRowScope` exclusively for horizontal `Weight`, never for vertical.
 */
val LocalRowScope = compositionLocalOf<RowScope?> { null }