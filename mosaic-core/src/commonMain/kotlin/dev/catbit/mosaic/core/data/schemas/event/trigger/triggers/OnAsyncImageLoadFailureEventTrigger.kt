package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnAsyncImageLoadFailure")
/** Fires on `AsyncImage` when Coil fails to load the image — can fire more than once over the
 * tile's lifetime, including on every reload triggered by `model` changing. */
object OnAsyncImageLoadFailureEventTrigger : EventTrigger