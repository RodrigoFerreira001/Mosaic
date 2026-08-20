package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Fires [onThresholdReached] when the last visible item of [lazyListState] comes within this many
 * items of the end of the list — the infinite-scroll pagination guard behind `LazyColumn`/`LazyRow`'s
 * `scrollThreshold` and their `OnScrollThresholdReached` trigger. Public and reusable outside the
 * built-in lazy tiles for a custom lazy-list-style tile that wants the same pagination behavior.
 *
 * The guard against re-firing on every recomposition while sitting past the threshold: it tracks the
 * item count at the last time it fired ([considerLoadingItemAtEnd] adds 1 to that baseline, to
 * account for a trailing loading placeholder item), and only fires again once
 * `lazyListState.layoutInfo.totalItemsCount` has actually grown past that baseline — so appending
 * items without also growing the list past the placeholder doesn't cause a second fire, and a list
 * that never grows only fires once total.
 *
 * @receiver the threshold: how many items from the end of the list counts as "reached".
 * @param lazyListState the list's own scroll state to observe.
 * @param considerLoadingItemAtEnd when `true` (the default), requires the list to have grown by more
 * than one item since the last fire before firing again — accounting for a trailing loading
 * placeholder that would otherwise count as "growth" on its own.
 * @param onThresholdReached called (from a `LaunchedEffect`) when the threshold is newly reached.
 */
@Composable
fun Int.ThresholdReachedEffect(
    lazyListState: LazyListState,
    considerLoadingItemAtEnd: Boolean = true,
    onThresholdReached: () -> Unit
) {
    var lastTriggeredItemCount by remember { mutableStateOf(0) }

    val reachedThreshold by remember {
        derivedStateOf {
            val lastVisibleIndex = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = lazyListState.layoutInfo.totalItemsCount
            if (totalItems == 0) false
            else (totalItems - lastVisibleIndex) <= this
        }
    }

    LaunchedEffect(reachedThreshold) {
        if (reachedThreshold) {
            val currentItemCount = lazyListState.layoutInfo.totalItemsCount
            if (currentItemCount > lastTriggeredItemCount.run { if (considerLoadingItemAtEnd) plus(1) else this }) {
                lastTriggeredItemCount = currentItemCount
                onThresholdReached()
            }
        }
    }
}
