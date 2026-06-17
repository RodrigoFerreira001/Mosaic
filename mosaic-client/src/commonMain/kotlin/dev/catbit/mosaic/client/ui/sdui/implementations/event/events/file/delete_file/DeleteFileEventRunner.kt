package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.delete_file

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.file.DeleteFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object DeleteFileEventRunner : EventRunner<DeleteFileEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: DeleteFileEventSchema) {
        println("executed DeleteFileEvent")
        onTrigger(EventTriggers.onSuccess())
    }
}
