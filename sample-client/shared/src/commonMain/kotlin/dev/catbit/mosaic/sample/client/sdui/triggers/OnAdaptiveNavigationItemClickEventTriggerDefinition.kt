package dev.catbit.mosaic.sample.client.sdui.triggers

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventTriggerDefinition
import dev.catbit.mosaic.sample.core.schemas.triggers.OnAdaptiveNavigationItemClickEventTrigger

object OnAdaptiveNavigationItemClickEventTriggerDefinition : EventTriggerDefinition<OnAdaptiveNavigationItemClickEventTrigger> {
    override val eventTriggerClass = OnAdaptiveNavigationItemClickEventTrigger::class
}