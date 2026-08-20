package dev.catbit.mosaic.client.ui.sdui.foundation.local_providers

import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.FlexBoxScope
import androidx.compose.runtime.compositionLocalOf

/**
 * Carries the innermost `FlexBox`'s Compose `FlexBoxScope` down the tree, or `null` when no ancestor
 * `FlexBox` has provided one — read by `Modifier.size()` to resolve a child's `Flex` sizing behavior
 * (grow/shrink/basis/align-self/order). Provided by `FlexBoxTileRenderer`; unlike
 * `LocalColumnScope`/`LocalRowScope`/`LocalLazyItemScope`, no other container renderer resets this
 * back to `null`, so it keeps propagating through any descendant subtree until another `FlexBox`
 * provides a fresh value.
 */
@OptIn(ExperimentalFlexBoxApi::class)
val LocalFlexBoxScope = compositionLocalOf<FlexBoxScope?> { null }
