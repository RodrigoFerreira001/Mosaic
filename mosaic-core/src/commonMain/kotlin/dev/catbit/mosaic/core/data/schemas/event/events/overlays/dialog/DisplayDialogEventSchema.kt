package dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Displays a dialog overlay populated with a server-defined tile tree. The dialog is shown
 * immediately when this event runs; no network call is made.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:** None. This event fires no lifecycle triggers; it only pushes the
 * overlay state to the screen via [ScreenTileBroadcastData.OnDisplayDialogRequested].
 *
 * **Failure scenarios:** None defined. The runner does not perform any fallible operation.
 *
 * **Notes:**
 * - [tiles] is loaded into the overlay editor before the broadcast is sent, so the dialog
 *   content is ready before the screen reacts to the broadcast.
 * - [isCancellable] controls whether the user can dismiss the dialog by tapping outside or
 *   pressing back; non-cancellable dialogs can only be closed via [DismissDialogEventSchema].
 * - [usePlatformDefaultWidth] defers the dialog width to the platform default when `true`;
 *   set to `false` to let the tile tree control its own width.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("DisplayDialog")
data class DisplayDialogEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    val tiles: SerializableImmutableList<TileSchema>,
    val isCancellable: Boolean,
    val usePlatformDefaultWidth: Boolean
) : EventSchema