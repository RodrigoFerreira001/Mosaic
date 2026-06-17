package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.save_file

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.file.SaveFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object SaveFileEventRunner : EventRunner<SaveFileEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: SaveFileEventSchema) {
        println("executed SaveFileEvent")
        onTrigger(EventTriggers.onSuccess())
    }
}
