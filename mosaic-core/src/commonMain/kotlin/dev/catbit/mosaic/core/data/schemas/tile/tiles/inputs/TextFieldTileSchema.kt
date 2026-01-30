package dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.Serializable

@Triggers(
    [
        dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTextChangedEventTrigger::class
    ]
)
@Serializable
data class TextFieldTileSchema(
    override val id: String,
    override val events: List<EventSchema>?,
    override val style: StyleSchema,
    override val visibility: TileSchema.Visibility,
    val value: String,
): TileSchema
