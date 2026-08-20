package dev.catbit.mosaic.client.ui.sdui.foundation.local_providers

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.compositionLocalOf

/**
 * Carries the innermost `Column`'s Compose `ColumnScope` down the tree, or `null` when no ancestor
 * `Column` has provided one yet — read by `Modifier.size()` to resolve a child's vertical `Weight`
 * sizing behavior. Provided by `ColumnTileRenderer`. `RowTileRenderer` doesn't reset this back to
 * `null`, so a `Row` nested inside a `Column` still sees the outer `Column`'s scope through it — this
 * only matters in practice because `Modifier.size()` checks `LocalColumnScope` exclusively for
 * *vertical* `Weight` (never for horizontal), so it doesn't affect a `Row`'s own horizontal weight
 * resolution, but a `Row`'s own child declaring a vertical `Weight` would resolve against the outer
 * `Column`'s scope rather than being ignored. A custom container tile that wants its children to
 * support `weight` should provide this the same way `Column` does.
 */
val LocalColumnScope = compositionLocalOf<ColumnScope?> { null }