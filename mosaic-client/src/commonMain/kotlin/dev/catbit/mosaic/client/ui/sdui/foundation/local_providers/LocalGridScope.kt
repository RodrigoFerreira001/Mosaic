package dev.catbit.mosaic.client.ui.sdui.foundation.local_providers

import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.GridScope
import androidx.compose.runtime.compositionLocalOf

@OptIn(ExperimentalGridApi::class)
val LocalGridScope = compositionLocalOf<GridScope?> { null }
