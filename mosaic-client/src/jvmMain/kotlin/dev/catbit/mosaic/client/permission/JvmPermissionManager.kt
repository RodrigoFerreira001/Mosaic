package dev.catbit.mosaic.client.permission

import dev.catbit.mosaic.client.ui.sdui.foundation.permission.PermissionManager
import dev.catbit.mosaic.client.ui.sdui.foundation.permission.PermissionResult
import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema.Permissions

class JvmPermissionManager : PermissionManager {
    override suspend fun requestPermissions(permissions: List<Permissions>) = PermissionResult.Granted
}
