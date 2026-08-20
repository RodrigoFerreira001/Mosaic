package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnSnackbarDismissed")
/** Fires when a snackbar shown via `DisplaySnackbar` goes away without its action button being
 * pressed — fires later, asynchronously, well after `DisplaySnackbar`'s own `onSuccess()`. */
object OnSnackbarDismissedEventTrigger : EventTrigger
