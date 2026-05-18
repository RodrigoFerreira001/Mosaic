package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPageChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnPageChangedEventTrigger::class
    ]
)
@Serializable
@SerialName("Pager")
data class PagerTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: List<TileSchema>,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("pageSize") val pageSize: PageSizeSchema = PageSizeSchema.Fill,
    @SerialName("pageSpacing") val pageSpacing: Int = 0,
    @SerialName("contentPadding") val contentPadding: Int = 0,
    @SerialName("beyondViewportPageCount") val beyondViewportPageCount: Int = 0
) : TileSchema {

    @Serializable
    sealed interface PageSizeSchema {

        @Serializable
        @SerialName("fill")
        data object Fill : PageSizeSchema

        @Serializable
        @SerialName("fixed")
        data class Fixed(@SerialName("value") val value: Int) : PageSizeSchema
    }
}
