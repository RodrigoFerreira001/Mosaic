package dev.catbit.mosaic.client.ui.sdui.foundation.definitions

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.core.data.event.EventModel
import kotlin.reflect.KClass

interface EventDefinition <Model: EventModel> {
    val eventModelClass: KClass<Model>
    val eventRunner: EventRunner<Model>
}