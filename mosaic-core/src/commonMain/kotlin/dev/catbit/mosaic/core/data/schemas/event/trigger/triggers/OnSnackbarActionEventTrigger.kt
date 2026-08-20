package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnSnackbarAction")
/** Fires when a snackbar shown via `DisplaySnackbar` has its action button pressed — fires later,
 * asynchronously, well after `DisplaySnackbar`'s own `onSuccess()` (which only confirms the show
 * broadcast was sent). */
object OnSnackbarActionEventTrigger : EventTrigger
