package dev.catbit.mosaic.core.data.schemas.event.events.security

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsAcquiredEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsDeniedEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnPermissionsAcquiredEventTrigger::class,
        OnPermissionsDeniedEventTrigger::class
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
        NOTIFICATION
    }
}