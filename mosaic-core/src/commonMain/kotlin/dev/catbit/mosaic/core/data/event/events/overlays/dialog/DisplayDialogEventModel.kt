package dev.catbit.mosaic.core.data.event.events.overlays.dialog

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.tile.TileModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("DisplayDialog")
data class DisplayDialogEventModel(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventModel>?,
    val tiles: List<TileModel>,
    val isCancellable: Boolean,
    val usePlatformDefaultWidth: Boolean
) : EventModel