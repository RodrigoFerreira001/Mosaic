package dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger::class,
        dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLongPressEventTrigger::class
    ]
)
@Serializable
@SerialName("Button")
data class ButtonTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("text") val text: String,
    @SerialName("loading") val loading: Boolean
) : TileSchema