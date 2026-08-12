package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSystemBroadcastEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableMap
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentMapOf
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
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>? = null,
    @SerialName("visibility") override val visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    @SerialName("navigationDrawerTiles") val navigationDrawerTiles: SerializableImmutableList<TileSchema>?,
    @SerialName("state") val state: State,
    @SerialName("stackableOverlays") val stackableOverlays: SerializableImmutableMap<String, StackableOverlay> = persistentMapOf(),
) : TileSchema {

    enum class State {
        INITIAL, DISPLAYING, FAILURE
    }

    @Immutable
    @Serializable
    sealed interface StackableOverlay {

        val tiles: ImmutableList<TileSchema>
        val isCancellable: Boolean
        val isDismissing: Boolean

        @Serializable
        @SerialName("BottomSheet")
        data class BottomSheet(
            @SerialName("tiles") override val tiles: ImmutableList<TileSchema>,
            @SerialName("isCancellable") override val isCancellable: Boolean,
            @SerialName("isDismissing") override val isDismissing: Boolean,
            @SerialName("fill") val fill: Boolean,
            @SerialName("allowsPartialExpansion") val allowsPartialExpansion: Boolean
        ) : StackableOverlay

        @Serializable
        @SerialName("ModalBottomSheet")
        data class ModalBottomSheet(
            @SerialName("tiles") override val tiles: ImmutableList<TileSchema>,
            @SerialName("isCancellable") override val isCancellable: Boolean,
            @SerialName("isDismissing") override val isDismissing: Boolean,
            @SerialName("fill") val fill: Boolean,
            @SerialName("allowsPartialExpansion") val allowsPartialExpansion: Boolean
        ) : StackableOverlay

        @Serializable
        @SerialName("Dialog")
        data class Dialog(
            @SerialName("tiles") override val tiles: ImmutableList<TileSchema>,
            @SerialName("isCancellable") override val isCancellable: Boolean,
            @SerialName("isDismissing") override val isDismissing: Boolean,
            @SerialName("usePlatformDefaultWidth") val usePlatformDefaultWidth: Boolean
        ) : StackableOverlay
    }
}