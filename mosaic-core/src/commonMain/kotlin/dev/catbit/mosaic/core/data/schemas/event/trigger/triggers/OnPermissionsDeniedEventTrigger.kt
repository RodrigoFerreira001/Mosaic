package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnPermissionsDenied")
/** Fires alongside `onFailure()` on `RequestPermission` when at least one requested permission was
 * denied. */
object OnPermissionsDeniedEventTrigger : EventTrigger