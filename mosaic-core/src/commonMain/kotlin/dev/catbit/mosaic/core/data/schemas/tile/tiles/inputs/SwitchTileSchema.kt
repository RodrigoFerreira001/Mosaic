package dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCheckChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnUncheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnUncheckEventTrigger::class,
        OnCheckEventTrigger::class,
        OnCheckChangedEventTrigger::class,
    ]
)
@Serializable
@SerialName("Switch")
data class SwitchTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("checked") val checked: Boolean,
    @SerialName("enabled") val enabled: Boolean
) : TileSchema
