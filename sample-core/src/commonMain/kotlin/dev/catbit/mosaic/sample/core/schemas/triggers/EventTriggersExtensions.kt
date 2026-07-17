package dev.catbit.mosaic.sample.core.schemas.triggers

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

fun EventTriggers.onAdaptiveNavigationItemClick(itemId: String) =
    OnAdaptiveNavigationItemClickEventTrigger(itemId)