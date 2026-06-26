package dev.catbit.mosaic.client.ui.sdui.foundation.permission

import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema

sealed class PermissionResult {
    data object Granted : PermissionResult()
    data object Denied : PermissionResult()
    data object Rationale : PermissionResult()
}

interface PermissionManager {
    suspend fun requestPermissions(
        permissions: List<RequestPermissionEventSchema.Permissions>
    ): PermissionResult
}
