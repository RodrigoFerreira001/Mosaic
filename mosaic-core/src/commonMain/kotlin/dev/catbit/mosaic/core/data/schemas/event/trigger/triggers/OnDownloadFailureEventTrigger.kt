package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnDownloadFailure")
/** Fires alongside `onFailure()` when `DownloadFile`/`DownloadFileToDisk`/`DownloadFileToMemory`
 * fails (not user cancellation — see `onCancelled()`), carrying the causing `Throwable` as incoming
 * data. */
object OnDownloadFailureEventTrigger : EventTrigger