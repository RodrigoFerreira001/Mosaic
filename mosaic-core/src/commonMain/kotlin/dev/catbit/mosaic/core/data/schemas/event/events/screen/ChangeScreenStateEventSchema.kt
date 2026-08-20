package dev.catbit.mosaic.core.data.schemas.event.events.screen

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Moves the screen this event lives in to another [State]: `Initial` (its loading state),
 * `Failure` (its failure state), or `Success` with the content to display.
 *
 * For `Success` the content comes from [State.Success.data] when declared, otherwise from the
 * event's incomingData, which must be a `ScreenModel` — typically the one produced by a preceding
 * `GetScreen`.
 *
 * **incomingData consumed:** used as the screen content when `Success` declares no data of its
 * own.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the state was applied. No data is passed downstream.
 * - `OnFailureEventTrigger` — when applying the state throws, including the case of `Success`
 *   without declared data and without a `ScreenModel` in incomingData; no data is passed
 *   downstream and the error is logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("ChangeScreenState")
data class ChangeScreenStateEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
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
                val tiles: SerializableImmutableList<TileSchema>,
                @SerialName("navigationDrawerTiles")
                val navigationDrawerTiles: SerializableImmutableList<TileSchema>?,
                @SerialName("events")
                val events: SerializableImmutableList<EventSchema>?
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