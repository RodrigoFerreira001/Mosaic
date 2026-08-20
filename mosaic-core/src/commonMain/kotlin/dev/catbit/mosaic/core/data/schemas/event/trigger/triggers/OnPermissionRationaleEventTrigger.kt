package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnPermissionRationale")
/** Fires on `RequestPermission` when the platform wants a rationale shown to the user before
 * asking again — real in practice only on Android, per its own runtime-permission APIs; other
 * platforms only ever produce `onPermissionsAcquired()`/`onPermissionsDenied()`. */
object OnPermissionRationaleEventTrigger : EventTrigger
