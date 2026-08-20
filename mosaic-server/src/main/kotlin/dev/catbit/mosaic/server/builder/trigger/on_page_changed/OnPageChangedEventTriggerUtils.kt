/**
 * DSL helpers for [OnPageChangedEventTrigger.Direction] — the `direction` argument to
 * `EventTriggers.onPageChanged(direction)`, e.g. `events = { Navigate(trigger =
 * EventTriggers.onPageChanged(onPageChangedToEnd()), ...) }`. Never construct/reference
 * `OnPageChangedEventTrigger.Direction` values directly — always go through these. Wiring several
 * directions on the same `Pager`/`Carousel` runs a separate chain for each one that matches a given
 * page change.
 */
package dev.catbit.mosaic.server.builder.trigger.on_page_changed

import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPageChangedEventTrigger

/** Matches landing on the first page/item. */
fun onPageChangedToStart() = OnPageChangedEventTrigger.Direction.Start
/** Matches landing on the last page/item. */
fun onPageChangedToEnd() = OnPageChangedEventTrigger.Direction.End
/** Matches every page change, regardless of which page. */
fun onPageChangedToAny() = OnPageChangedEventTrigger.Direction.Any
/** Matches landing on exactly [index].
 * @param index the page/item index to match. */
fun onPageChangedToIndex(index: Int) = OnPageChangedEventTrigger.Direction.Index(index)