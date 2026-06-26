package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.security.request_permission

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.foundation.permission.PermissionManager
import dev.catbit.mosaic.client.ui.sdui.foundation.permission.PermissionResult
import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object RequestPermissionEventRunner : EventRunner<RequestPermissionEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: RequestPermissionEventSchema) {
        val manager = get<PermissionManager>()
        when (manager.requestPermissions(event.permissions)) {
            PermissionResult.Granted -> {
                onTrigger(EventTriggers.onPermissionsAcquired())
                onTrigger(EventTriggers.onSuccess())
            }
            PermissionResult.Rationale -> {
                onTrigger(EventTriggers.onPermissionRationale())
            }
            PermissionResult.Denied -> {
                onTrigger(EventTriggers.onPermissionsDenied())
                onTrigger(EventTriggers.onFailure())
            }
        }
    }
}
