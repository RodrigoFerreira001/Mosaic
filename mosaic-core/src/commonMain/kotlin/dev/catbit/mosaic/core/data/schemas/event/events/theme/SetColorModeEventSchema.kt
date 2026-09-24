package dev.catbit.mosaic.core.data.schemas.event.events.theme

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sets the app's color mode — light, dark, or following the system setting — overriding whatever
 * was set before. Unlike `SetTheme`, which swaps the Material color roles themselves, this only
 * decides which of the already-installed light/dark schemes is active.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — always, after the color mode is applied. No data is passed
 *   downstream.
 */
@Immutable
@Triggers([OnSuccessEventTrigger::class])
@Serializable
@SerialName("SetColorMode")
data class SetColorModeEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("colorMode") val colorMode: ColorMode
) : EventSchema {

    @Serializable
    enum class ColorMode {
        @SerialName("Light")
        Light,
        @SerialName("Dark")
        Dark,
        @SerialName("SystemDefault")
        SystemDefault
    }
}
