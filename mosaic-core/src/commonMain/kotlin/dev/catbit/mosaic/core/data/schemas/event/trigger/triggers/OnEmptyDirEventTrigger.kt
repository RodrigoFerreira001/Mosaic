package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnEmptyDir")
/** Fires when `ListFiles` finds the requested directory but it holds no files. No data is passed
 * downstream. */
object OnEmptyDirEventTrigger : EventTrigger
