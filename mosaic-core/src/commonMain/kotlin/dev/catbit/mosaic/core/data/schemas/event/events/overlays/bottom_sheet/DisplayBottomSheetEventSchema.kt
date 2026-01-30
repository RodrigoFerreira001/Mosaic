package dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("DisplayBottomSheet")
data class DisplayBottomSheetEventSchema( // TODO ter aqui um identificador real para a BS, para fins de eventos de dismiss
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    val tiles: List<TileSchema>,
    val isCancellable: Boolean,
    val fill: Boolean
) : EventSchema