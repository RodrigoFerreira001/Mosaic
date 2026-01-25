package dev.catbit.mosaic.core.data.event.events.security

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnPermissionsAcquiredEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnPermissionsDeniedEventTrigger
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
data class RequestPermissionEventModel(
    override val id: String,
    override val trigger: EventTrigger,
    override val events: List<EventModel>?,
    val permissions: List<Permissions>
) : EventModel {

    enum class Permissions {
        CAMERA,
        GALLERY,
        STORAGE,
        MICROPHONE,
        LOCATION,
        NOTIFICATION
    }
}