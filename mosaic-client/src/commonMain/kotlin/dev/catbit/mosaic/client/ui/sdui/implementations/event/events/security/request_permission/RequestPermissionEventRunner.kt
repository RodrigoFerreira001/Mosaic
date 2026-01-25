package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.security.request_permission

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.events.security.RequestPermissionEventModel

object RequestPermissionEventRunner : EventRunner<RequestPermissionEventModel> {
    override suspend fun EventRunningScope.runEvent(event: RequestPermissionEventModel) {
        println("executed RequestPermissionEventModel")
    }
}
