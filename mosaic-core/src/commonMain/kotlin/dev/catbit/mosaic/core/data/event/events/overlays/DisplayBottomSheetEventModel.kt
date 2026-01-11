package dev.catbit.mosaic.core.data.event.events.overlays

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("DisplayBottomSheet")
data class DisplayBottomSheetEventModel( // TODO ter aqui um identificador real para a BS, para fins de eventos de dismiss
    val tiles: List<TileModel>,
    val isCancellable: Boolean,
    val fill: Boolean,
    override val id: String,
    override val trigger: EventTrigger,
    override val events: List<EventModel>?
) : EventModel