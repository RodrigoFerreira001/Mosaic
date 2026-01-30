package dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("DisplayDialog")
data class DisplayDialogEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    val tiles: List<TileSchema>,
    val isCancellable: Boolean,
    val usePlatformDefaultWidth: Boolean
) : EventSchema