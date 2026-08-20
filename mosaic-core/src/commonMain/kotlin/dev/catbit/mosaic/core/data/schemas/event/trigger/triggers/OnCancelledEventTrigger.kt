package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnCancelled")
/** Fires when the user dismisses/cancels an interactive platform flow without completing it —
 * `OpenFilePicker`/`TakePicture`/`GetImageFromGallery` (no choice made) and `DownloadFile` (the
 * platform save dialog cancelled), instead of the matching `onFailure()`. */
object OnCancelledEventTrigger : EventTrigger