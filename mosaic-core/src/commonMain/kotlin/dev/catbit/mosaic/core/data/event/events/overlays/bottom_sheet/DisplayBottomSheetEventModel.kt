package dev.catbit.mosaic.core.data.event.events.overlays.bottom_sheet

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.tile.TileModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("DisplayBottomSheet")
data class DisplayBottomSheetEventModel( // TODO ter aqui um identificador real para a BS, para fins de eventos de dismiss
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventModel>?,
    val tiles: List<TileModel>,
    val isCancellable: Boolean,
    val fill: Boolean
) : EventModel