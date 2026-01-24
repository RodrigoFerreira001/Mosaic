package dev.catbit.mosaic.core.data.tile.tiles.inputs

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnTextChangedEventTrigger
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnTextChangedEventTrigger::class
    ]
)
@Serializable
data class TextFieldTileModel(
    override val id: String,
    override val events: List<EventModel>?,
    override val style: StyleModel,
    override val visibility: TileModel.Visibility,
    val value: String,
): TileModel
