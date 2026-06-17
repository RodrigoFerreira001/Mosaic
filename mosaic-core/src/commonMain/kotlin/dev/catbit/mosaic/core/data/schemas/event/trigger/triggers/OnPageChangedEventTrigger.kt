package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnPageChanged")
data class OnPageChangedEventTrigger(
    @SerialName("direction") val direction: Direction
) : EventTrigger {

    @Serializable
    sealed class Direction {
        @Serializable @SerialName("Start") data object Start : Direction()
        @Serializable @SerialName("End")   data object End   : Direction()
        @Serializable @SerialName("Any")   data object Any   : Direction()
        @Serializable @SerialName("Index") data class  Index(
            @SerialName("index") val index: Int
        ) : Direction()
    }
}