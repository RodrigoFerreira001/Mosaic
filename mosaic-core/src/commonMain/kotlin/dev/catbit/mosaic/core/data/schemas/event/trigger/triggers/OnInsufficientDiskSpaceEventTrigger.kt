package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnInsufficientDiskSpace")
/** Fires when `DownloadFileToDisk` fails because the device has no free storage left to hold the
 * file, carrying the causing `Throwable` as incoming data. Replaces `onDownloadFailure()` and
 * `onFailure()` for that failure — neither of them fires — and the partially written file is
 * deleted before it fires. */
object OnInsufficientDiskSpaceEventTrigger : EventTrigger
