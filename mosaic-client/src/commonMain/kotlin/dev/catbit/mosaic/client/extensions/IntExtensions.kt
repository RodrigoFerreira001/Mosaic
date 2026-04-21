package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

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
