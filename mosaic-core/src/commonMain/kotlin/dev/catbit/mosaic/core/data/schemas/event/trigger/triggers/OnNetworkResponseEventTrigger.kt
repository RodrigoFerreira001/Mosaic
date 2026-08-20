package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnNetworkResponse")
/** Fires on `SendNetworkRequest`/`UploadFile` instead of `onSuccess()` when a child is wired to the
 * exact HTTP status code the response came back with.
 * @property httpCode the exact status code this trigger matches. */
data class OnNetworkResponseEventTrigger(
    @SerialName("httpCode") val httpCode: Int
) : EventTrigger