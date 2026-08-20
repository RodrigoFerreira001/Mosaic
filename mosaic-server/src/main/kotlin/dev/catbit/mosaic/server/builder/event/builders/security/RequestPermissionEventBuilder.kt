package dev.catbit.mosaic.server.builder.event.builders.security

import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema.Permissions
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import kotlinx.collections.immutable.toImmutableList

internal class RequestPermissionEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val permissions: List<Permissions>
) : EventSchemaBuilder<RequestPermissionEventSchema>() {

    override fun build() = RequestPermissionEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        permissions = permissions.toImmutableList()
    )
}

/**
 * Asks the platform for the runtime [permissions], through the client's `PermissionManager`.
 * Does not consume `incomingData`. Dispatches `onPermissionsAcquired` then `onSuccess` (both no
 * data) when every requested permission was granted; `onPermissionsDenied` then `onFailure`
 * (both no data) when the request was denied; `onPermissionRationale` (no data) when the platform
 * asks for a rationale to be shown before requesting again — chain the explanation onto this
 * trigger and request once more.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onPermissionsAcquired`, `onPermissionsDenied`, `onPermissionRationale`, `onSuccess`, `onFailure`).
 * @param permissions Runtime permissions requested, built with [cameraPermission], [galleryPermission], [storagePermission], [microphonePermission], [locationPermission], [notificationPermission] or [contactsPermission].
 */
fun EventSchemaBuilderScope.RequestPermission(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    permissions: List<Permissions>
) {
    addBuilder(
        RequestPermissionEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            permissions = permissions
        )
    )
}

/** Camera access permission. */
fun cameraPermission() = Permissions.CAMERA

/** Photo gallery / media library access permission. */
fun galleryPermission() = Permissions.GALLERY

/** Generic file storage access permission. */
fun storagePermission() = Permissions.STORAGE

/** Microphone access permission. */
fun microphonePermission() = Permissions.MICROPHONE

/** Location access permission. */
fun locationPermission() = Permissions.LOCATION

/** Push notification permission. */
fun notificationPermission() = Permissions.NOTIFICATION

/** Contacts access permission. */
fun contactsPermission() = Permissions.CONTACTS
