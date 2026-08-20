package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnDownloadProgress")
/** Fires repeatedly on `DownloadFile`/`DownloadFileToDisk`/`DownloadFileToMemory` as the download
 * progresses, carrying progress as incoming data. */
object OnDownloadProgressEventTrigger : EventTrigger