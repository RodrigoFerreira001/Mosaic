package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.get_file

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.file.GetFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object GetFileEventRunner : EventRunner<GetFileEventSchema> {
    override fun EventRunningScope.runEvent(event: GetFileEventSchema) {
        println("executed GetFileEvent")
        onTrigger(EventTriggers.onSuccess())
    }
}
