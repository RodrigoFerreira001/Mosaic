/**
 * DSL helpers for [OnScrolledEventTrigger.ScrollDirection] — the `direction` argument to
 * `EventTriggers.onScrolled(direction)`, e.g. `events = { UpdateTiles(trigger =
 * EventTriggers.onScrolled(onScrolledToTop()), ...) }`. Never construct/reference
 * `OnScrolledEventTrigger.ScrollDirection` values directly — always go through these.
 */
package dev.catbit.mosaic.server.builder.trigger.on_scrolled

import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger

/** Matches a `Column`/`LazyColumn` scrolling backward, toward the start of the list. */
fun onScrolledToTop() = OnScrolledEventTrigger.ScrollDirection.Top
/** Matches a `Column`/`LazyColumn` scrolling forward, toward the end of the list. */
fun onScrolledToBottom() = OnScrolledEventTrigger.ScrollDirection.Bottom
/** Matches a `Row`/`LazyRow` scrolling backward. */
fun onScrolledToStart() = OnScrolledEventTrigger.ScrollDirection.Start
/** Matches a `Row`/`LazyRow` scrolling forward. */
fun onScrolledToEnd() = OnScrolledEventTrigger.ScrollDirection.End