package dev.catbit.mosaic.core.data.schemas.event.events.security

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionRationaleEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsAcquiredEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsDeniedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Asks the platform for the runtime permissions listed in [permissions], through the client's
 * `PermissionManager`.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnPermissionsAcquiredEventTrigger` then `OnSuccessEventTrigger` — when every requested
 *   permission was granted.
 * - `OnPermissionsDeniedEventTrigger` then `OnFailureEventTrigger` — when the request was denied.
 * - `OnPermissionRationaleEventTrigger` — when the platform asks for a rationale to be shown
 *   before requesting again; chain the explanation onto this trigger and request once more.
 *
 * No data is passed downstream in any case.
 */
@Immutable
@Triggers(
    [
        OnPermissionsAcquiredEventTrigger::class,
        OnPermissionsDeniedEventTrigger::class,
        OnPermissionRationaleEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("RequestPermission")
data class RequestPermissionEventSchema(
    override val id: String,
    override val trigger: EventTrigger,
    override val events: SerializableImmutableList<EventSchema>?,
    val permissions: SerializableImmutableList<Permissions>
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
