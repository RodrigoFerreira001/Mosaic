package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnStart")
/** Fires on an event right before its real (typically async/network) work begins — lets a child
 * react to "the operation started" separately from its eventual outcome (e.g. showing a loading
 * spinner). Declared by most networking and data-reading events (`SendNetworkRequest`, `UploadFile`,
 * `GetData`, `GetScreen`/`RefreshScreen`, the download events). */
object OnStartEventTrigger : EventTrigger