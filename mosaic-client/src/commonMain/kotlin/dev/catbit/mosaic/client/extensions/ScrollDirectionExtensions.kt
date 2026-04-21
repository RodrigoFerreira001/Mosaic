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
