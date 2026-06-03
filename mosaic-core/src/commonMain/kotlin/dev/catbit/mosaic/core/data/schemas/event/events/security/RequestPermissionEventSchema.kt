package dev.catbit.mosaic.core.data.schemas.event.events.security

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsAcquiredEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsDeniedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Requests one or more runtime permissions from the user on behalf of the server-driven UI.
 * The runner is currently a placeholder (`println`) — the actual platform permission request
 * has not yet been implemented.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired (intended, not yet implemented):**
 * - [OnPermissionsAcquiredEventTrigger] — intended to fire when the user grants all requested
 *   [permissions].
 * - [OnPermissionsDeniedEventTrigger] — intended to fire when the user denies one or more of
 *   the requested [permissions], or when a permission is permanently denied.
 *
 * **Failure scenarios:** Not applicable — the runner is a no-op placeholder.
 *
 * **Notes:** [permissions] is a list drawn from [Permissions] (CAMERA, GALLERY, STORAGE,
 * MICROPHONE, LOCATION, NOTIFICATION, CONTACTS). Multiple permissions can be requested in a
 * single event. The platform-specific permission dialog flow and the mapping between
 * [Permissions] enum values and actual Android/iOS permission strings are not yet implemented.
 */
@Triggers(
    [
        OnPermissionsAcquiredEventTrigger::class,
        OnPermissionsDeniedEventTrigger::class,
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("RequestPermission")
data class RequestPermissionEventSchema(
    override val id: String,
    override val trigger: EventTrigger,
    override val events: List<EventSchema>?,
    val permissions: List<Permissions>
) : EventSchema {

    enum class Permissions {
        CAMERA,
        GALLERY,
        STORAGE,
        MICROPHONE,
        LOCATION,
        NOTIFICATION,
        CONTACTS
    }
}