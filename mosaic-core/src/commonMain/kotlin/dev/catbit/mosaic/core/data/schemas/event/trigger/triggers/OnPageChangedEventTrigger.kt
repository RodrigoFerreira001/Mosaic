package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnPageChanged")
/**
 * Fires on `Pager`/`Carousel` when the current page/item settles on a new one — once per matching
 * [direction] per change, so wiring several directions runs several chains for the same page change.
 * The initial page doesn't fire this. Carries the new page/item index (an `Int`) as `incomingData` —
 * a mixed-content template like `"Page: <||>"` resolves against it directly.
 *
 * @property direction which change this trigger matches.
 */
data class OnPageChangedEventTrigger(
    @SerialName("direction") val direction: Direction
) : EventTrigger {

    /** Which kind of page change this trigger matches. */
    @Serializable
    sealed class Direction {
        /** Matches landing on the first page/item. */
        @Serializable @SerialName("Start") data object Start : Direction()
        /** Matches landing on the last page/item. */
        @Serializable @SerialName("End")   data object End   : Direction()
        /** Matches every page change, regardless of which page. */
        @Serializable @SerialName("Any")   data object Any   : Direction()
        /** Matches landing on exactly [index].
         * @property index the page/item index this matches. */
        @Serializable @SerialName("Index") data class  Index(
            @SerialName("index") val index: Int
        ) : Direction()
    }
}