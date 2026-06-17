package dev.catbit.mosaic.core.data.schemas.event.events.screen

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Immediately transitions the current screen to one of three visual states — [State.Success],
 * [State.Failure], or [State.Initial] — without making any network call.
 *
 * **incomingData consumed:** Only relevant when [state] is [State.Success] and its embedded
 * [State.Success.data] is `null`. In that case, incomingData is cast to `ScreenModel` and used
 * as the screen content. If both [State.Success.data] is `null` and incomingData cannot be cast
 * to `ScreenModel`, an `IllegalArgumentException` is thrown at runtime.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — when the screen state is successfully applied.
 * - [OnFailureEventTrigger] — when [state] is [State.Success] with `data == null` and incomingData
 *   cannot be cast to `ScreenModel`; incomingData is the `IllegalArgumentException`.
 *
 * **Failure scenarios:**
 * - [State.Success] with `data == null` and incomingData not castable to `ScreenModel`:
 *   throws `IllegalArgumentException("ChangeScreenState needs a valid ScreenModel and State is Success")`
 *   and fires [OnFailureEventTrigger] with the exception.
 *
 * **Notes:**
 * - When [state] is [State.Success] and [State.Success.data] is non-null, the screen content
 *   is fully inline in the schema and incomingData is ignored entirely.
 * - This event is the typical consumer of the `ScreenModel` produced by [GetScreenEventSchema]
 *   or [RefreshScreenEventSchema] when chained via [onSuccess()].
 * - [State.Initial] resets the screen to its loading/initial appearance; [State.Failure] renders
 *   the error state.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
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