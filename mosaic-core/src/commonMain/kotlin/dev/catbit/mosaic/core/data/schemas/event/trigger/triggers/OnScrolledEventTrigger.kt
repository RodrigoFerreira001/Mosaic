package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnScrolled")
/**
 * Fires on `Column`/`Row` (when `scrollable = true`) and `LazyColumn`/`LazyRow` whenever the scroll
 * direction changes — not on every scroll delta. `Column`/`LazyColumn` only ever produce
 * [ScrollDirection.Top]/[ScrollDirection.Bottom]; `Row`/`LazyRow` only ever produce
 * [ScrollDirection.Start]/[ScrollDirection.End].
 *
 * @property direction which direction this trigger matches.
 */
data class OnScrolledEventTrigger(
    @SerialName("direction") val direction: ScrollDirection
) : EventTrigger {

    /** Which way the scroll position just moved. */
    @Serializable
    enum class ScrollDirection {
        /** Scrolling backward on a `Column`/`LazyColumn` (toward the start of the list). */
        @SerialName("Top") Top,
        /** Scrolling forward on a `Column`/`LazyColumn` (toward the end of the list). */
        @SerialName("Bottom") Bottom,
        /** Scrolling backward on a `Row`/`LazyRow`. */
        @SerialName("Start") Start,
        /** Scrolling forward on a `Row`/`LazyRow`. */
        @SerialName("End") End
    }
}
