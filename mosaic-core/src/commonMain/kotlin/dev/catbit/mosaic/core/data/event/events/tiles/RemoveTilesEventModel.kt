package dev.catbit.mosaic.core.data.event.events.tiles

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("RemoveTiles")
data class RemoveTilesEventModel(
    val groupingTileId: String?,
    val tileIds: List<String>,
    override val id: String,
    override val trigger: EventTrigger,
    override val events: List<EventModel>?
) : EventModel
