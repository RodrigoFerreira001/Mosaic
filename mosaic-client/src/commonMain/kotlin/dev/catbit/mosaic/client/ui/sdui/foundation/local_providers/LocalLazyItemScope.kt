package dev.catbit.mosaic.client.ui.sdui.foundation.local_providers

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.compositionLocalOf

/**
 * Carries the innermost `LazyColumn`/`LazyRow` item's Compose `LazyItemScope` down the tree, or
 * `null` when the current tile isn't a direct item of a lazy list — read by `Modifier.size()`, checked
 * **before** `LocalColumnScope`/`LocalRowScope`/`LocalFlowRowScope`, so a `Weight`-sized child of a
 * `LazyColumn`/`LazyRow` gets `fillParentMaxWidth`/`Height` treatment (the closest lazy-list
 * equivalent) instead of a `ColumnScope`/`RowScope` `weight`, consistent with lazy lists never
 * publishing those scopes to their children. Provided by `LazyColumnTileRenderer`/
 * `LazyRowTileRenderer`; reset to `null` by every non-lazy container renderer
 * (`Column`/`Row`/`Card`/`FlowRow`).
 */
val LocalLazyItemScope = compositionLocalOf<LazyItemScope?> { null }