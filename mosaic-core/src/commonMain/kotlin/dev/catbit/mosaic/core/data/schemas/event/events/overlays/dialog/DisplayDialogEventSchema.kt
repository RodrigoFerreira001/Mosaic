package dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Shows a dialog built from [tiles], registered under [dialogId] so a later `DismissDialog` can
 * close it.
 *
 * [isCancellable] decides whether the user can dismiss it with the back gesture or a scrim tap,
 * and [usePlatformDefaultWidth] whether the dialog keeps the platform's default width or is free
 * to size itself from its content.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the dialog was added. No data is passed downstream.
 * - `OnFailureEventTrigger` — when it could not be added, typically because [dialogId] is already
 *   in use; the `Throwable` is passed as incomingData.
 * - `OnDisplayEventTrigger` — once, when the dialog actually enters composition on screen.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
        OnDisplayEventTrigger::class,
    ]
)
@Serializable
@SerialName("DisplayDialog")
data class DisplayDialogEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("dialogId") val dialogId: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("isCancellable") val isCancellable: Boolean,
    @SerialName("usePlatformDefaultWidth") val usePlatformDefaultWidth: Boolean
) : EventSchema