package dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnClickEventTrigger::class
    ]
)
@Serializable
@SerialName("IconButton")
data class IconButtonTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("icon") val icon: IconSchema,
    @SerialName("buttonType") val buttonType: Type = Type.DEFAULT,
    @SerialName("enabled") val enabled: Boolean,
) : TileSchema {

    enum class Type {
        DEFAULT,
        FILLED,
        FILLED_TONAL,
        OUTLINED,
    }
}
