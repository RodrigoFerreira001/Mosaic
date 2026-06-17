package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSystemBroadcastEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnSystemBroadcastEventTrigger::class
    ]
)
@Immutable
@Serializable
data class ScreenTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema = StyleSchema.default(),
    @SerialName("visibility") override val visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    @SerialName("navigationDrawerTiles") val navigationDrawerTiles: SerializableImmutableList<TileSchema>?,
    @SerialName("currentBottomSheetTiles") val currentBottomSheetTiles: SerializableImmutableList<TileSchema>? = null,
    @SerialName("currentDialogSheetTiles") val currentDialogSheetTiles: SerializableImmutableList<TileSchema>? = null,
    @SerialName("state") val state: State
) : TileSchema {
    enum class State {
        INITIAL, DISPLAYING, FAILURE
    }
}