package dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLongPressEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnClickEventTrigger::class,
        OnLongPressEventTrigger::class
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
    @SerialName("icon") val icon: IconSchema? = null,
    @SerialName("buttonType") val buttonType: Type = Type.FILLED,
    @SerialName("shape") val shape: Shape = Shape.ROUNDED,
    @SerialName("loading") val loading: Boolean = false,
    @SerialName("enabled") val enabled: Boolean = true
) : TileSchema {

    enum class Type {
        FILLED,
        ELEVATED,
        FILLED_TONAL,
        OUTLINED,
        TEXT
    }

    enum class Shape {
        SQUARE, ROUNDED
    }
}