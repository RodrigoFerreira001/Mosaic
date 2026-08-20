package dev.catbit.mosaic.client.ui.sdui.foundation.local_providers

import androidx.compose.runtime.compositionLocalOf
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.TilesManager

/**
 * Carries the current screen's `TilesManager` down the tree, or `null` outside any screen — provided
 * by `MosaicScreen`. This is what makes the nested-navigation-graph pattern possible: a custom
 * container tile that hosts its own mini backstack composes its own
 * `MosaicScreen(parent = LocalTilesManager.current)`, so lookups that miss inside the nested tree can
 * recurse upward into the hosting screen's own `TilesManager` (see `NestedNavigationGraph` for the
 * built-in example).
 */
val LocalTilesManager = compositionLocalOf<TilesManager?> { null }