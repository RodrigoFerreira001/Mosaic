package dev.catbit.mosaic.server.builder.trigger.on_scrolled

import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger

fun onScrolledToTop() = OnScrolledEventTrigger.ScrollDirection.Top
fun onScrolledToBottom() = OnScrolledEventTrigger.ScrollDirection.Bottom
fun onScrolledToStart() = OnScrolledEventTrigger.ScrollDirection.Start
fun onScrolledToEnd() = OnScrolledEventTrigger.ScrollDirection.End