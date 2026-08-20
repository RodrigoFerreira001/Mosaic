package dev.catbit.mosaic.client.ui.sdui.foundation.permission

import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema

/** Outcome of a [PermissionManager.requestPermissions] call — mirrors the 3 outcomes
 * `RequestPermission` fires triggers for (`OnPermissionsAcquired`/`OnPermissionsDenied`/
 * `OnPermissionRationale`). */
sealed class PermissionResult {
    /** Every requested permission was granted. */
    data object Granted : PermissionResult()
    /** At least one requested permission was denied. */
    data object Denied : PermissionResult()
    /** The platform wants a rationale shown to the user before asking again — real in practice only
     * on Android, per its own runtime-permission APIs; other platforms only ever produce [Granted]/
     * [Denied]. */
    data object Rationale : PermissionResult()
}

/**
 * Platform abstraction over runtime permission requests — the collaborator behind the
 * `RequestPermission` event. Bound once per platform target in that target's own `platformModule`,
 * so `RequestPermissionEventRunner` can reach it via `get<PermissionManager>()` without knowing which
 * platform it's running on.
 */
interface PermissionManager {
    /**
     * Requests every permission in [permissions] from the platform.
     *
     * @param permissions the permissions to request — camera, gallery, storage, microphone,
     * location, notification, contacts.
     * @return the outcome — [PermissionResult.Granted] only if every requested permission was
     * granted.
     */
    suspend fun requestPermissions(
        permissions: List<RequestPermissionEventSchema.Permissions>
    ): PermissionResult
}
