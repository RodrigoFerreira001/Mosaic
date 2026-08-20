package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow

/**
 * Observes this [LazyListState]'s scroll position and calls [onScrollForward]/[onScrollBackward]
 * every time the scroll direction actually changes (not on every scroll delta) — the real mechanism
 * behind `LazyColumn`/`LazyRow`'s `OnScrolled` trigger (`ScrollDirection.Bottom`/`End` maps to
 * [onScrollForward], `ScrollDirection.Top`/`Start` to [onScrollBackward]).
 *
 * Direction is derived by comparing the current `firstVisibleItemIndex`/`firstVisibleItemScrollOffset`
 * pair against the previous one on every emission of `snapshotFlow`; a frame where neither changes
 * calls neither callback.
 *
 * @param onScrollForward called when the list's first visible item moves later (scrolling toward the
 * end of the list).
 * @param onScrollBackward called when it moves earlier (scrolling toward the start).
 */
@Composable
fun LazyListState.ObserveScrollDirection(
    onScrollForward: () -> Unit,
    onScrollBackward: () -> Unit,
) {
    var previousIndex by remember { mutableIntStateOf(firstVisibleItemIndex) }
    var previousOffset by remember { mutableIntStateOf(firstVisibleItemScrollOffset) }

    LaunchedEffect(Unit) {
        snapshotFlow { firstVisibleItemIndex to firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                if (index > previousIndex || (index == previousIndex && offset > previousOffset)) {
                    onScrollForward()
                } else if (index < previousIndex || (index == previousIndex && offset < previousOffset)) {
                    onScrollBackward()
                }
                previousIndex = index
                previousOffset = offset
            }
    }
}

/**
 * Same as the [LazyListState] overload above, for a plain (non-lazy) [ScrollState] — the mechanism
 * behind `Column`/`Row`'s own `OnScrolled` trigger when `scrollable = true`. Direction is derived by
 * comparing the current scroll offset ([ScrollState.value]) against the previous one.
 *
 * @param onScrollForward called when the scroll offset increases.
 * @param onScrollBackward called when it decreases.
 */
@Composable
fun ScrollState.ObserveScrollDirection(
    onScrollForward: () -> Unit,
    onScrollBackward: () -> Unit,
) {
    var previousValue by remember { mutableIntStateOf(value) }

    LaunchedEffect(Unit) {
        snapshotFlow { value }
            .collect { currentValue ->
                if (currentValue > previousValue) {
                    onScrollForward()
                } else if (currentValue < previousValue) {
                    onScrollBackward()
                }
                previousValue = currentValue
            }
    }
}
