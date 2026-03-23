package dev.catbit.mosaic.core.data.schemas.tile.tiles.image

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAsyncImageLoadFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAsyncImageLoadStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAsyncImageLoadSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnAsyncImageLoadFailureEventTrigger::class,
        OnAsyncImageLoadStartEventTrigger::class,
        OnAsyncImageLoadSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("AsyncImage")
data class AsyncImageTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("url") val url: String,
    @SerialName("contentDescription") val contentDescription: String?,
    @SerialName("contentScale") val contentScale: ContentScale,
    @SerialName("alpha") val alpha: Float,
    @SerialName("clipToBounds") val clipToBounds: Boolean,
    @SerialName("alignment") val alignment: AlignmentSchema.TwoDimensional
) : TileSchema {

    enum class ContentScale {
        CROP,
        FIT,
        FILL_HEIGHT,
        FILL_WIDTH,
        INSIDE,
        FILL_BOUNDS,
    }
}

