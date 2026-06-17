package dev.catbit.mosaic.client.ui.sdui.foundation.definitions

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlin.reflect.KClass

interface EventTriggerDefinition<T : EventTrigger> {
    val eventTriggerClass: KClass<T>
}