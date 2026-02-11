package dev.catbit.mosaic.core.data.schemas.event.events.screen

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ChangeScreenState")
data class ChangeScreenStateEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("state") val state: State
) : EventSchema {

    @Serializable
    sealed interface State {

        @SerialName("Success")
        @Serializable
        data class Success(
            @SerialName("data")
            val data: ScreenData?
        ) : State {
            @Serializable
            data class ScreenData(
                @SerialName("tiles")
                val tiles: List<TileSchema>,
                @SerialName("navigationDrawerTiles")
                val navigationDrawerTiles: List<TileSchema>?,
                @SerialName("events")
                val events: List<EventSchema>?
            )
        }

        @SerialName("Failure")
        @Serializable
        data object Failure : State

        @SerialName("Initial")
        @Serializable
        data object Initial : State
    }
}