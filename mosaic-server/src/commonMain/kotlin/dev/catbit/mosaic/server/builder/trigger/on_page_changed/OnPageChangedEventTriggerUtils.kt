package dev.catbit.mosaic.server.builder.trigger.on_page_changed

import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPageChangedEventTrigger

fun onPageChangedToStart() = OnPageChangedEventTrigger.Direction.Start
fun onPageChangedToEnd() = OnPageChangedEventTrigger.Direction.End
fun onPageChangedToAny() = OnPageChangedEventTrigger.Direction.Any
fun onPageChangedToIndex(index: Int) = OnPageChangedEventTrigger.Direction.Index(index)