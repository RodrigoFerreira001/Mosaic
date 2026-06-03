package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.security.request_permission

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object RequestPermissionEventRunner : EventRunner<RequestPermissionEventSchema> {
    override fun EventRunningScope.runEvent(event: RequestPermissionEventSchema) {
        println("executed RequestPermissionEventModel")
        onTrigger(EventTriggers.onSuccess())
    }
}
