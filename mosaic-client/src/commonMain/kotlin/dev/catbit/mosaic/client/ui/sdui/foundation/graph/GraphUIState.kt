package dev.catbit.mosaic.client.ui.sdui.foundation.graph

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema

@Immutable
data class GraphUIState(
    val entries: List<Entry>,
    val startEntryId: String,
    val defaultTransition: ContentTransitionSchema? = null,
    val defaultPopTransition: ContentTransitionSchema? = null,
    val defaultPredictivePopTransition: ContentTransitionSchema? = null,
) {
    data class Entry(
        val screenId: String,
        val transition: ContentTransitionSchema? = null,
        val popTransition: ContentTransitionSchema? = null,
        val predictivePopTransition: ContentTransitionSchema? = null,
    )
}
