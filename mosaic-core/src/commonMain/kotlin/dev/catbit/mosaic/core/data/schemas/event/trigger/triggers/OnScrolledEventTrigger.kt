package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnScrolled")
data class OnScrolledEventTrigger(
    @SerialName("direction") val direction: ScrollDirection
) : EventTrigger {

    @Serializable
    enum class ScrollDirection {
        @SerialName("Top") Top,
        @SerialName("Bottom") Bottom,
        @SerialName("Start") Start,
        @SerialName("End") End
    }
}
