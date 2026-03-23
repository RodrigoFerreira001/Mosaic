package dev.catbit.mosaic.core.data.schemas.tile.tiles.search

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTextChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnTextChangedEventTrigger::class
    ]
)
@Serializable
@SerialName("SearchBar")
data class SearchBarTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("query") val query: String = "",
    @SerialName("placeholder") val placeholder: String? = null,
    @SerialName("leadingIcon") val leadingIcon: TileSchema? = null,
    @SerialName("trailingIcon") val trailingIcon: TileSchema? = null
) : TileSchema
