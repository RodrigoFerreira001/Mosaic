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
 * Reverts a previous [SetThemeEventSchema] override, restoring the app's default Material3 color
 * scheme (light and dark) for both mode variants.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — always, once the default color scheme has been restored.
 *
 * **Notes:** Has no effect (besides re-confirming the default) if [SetThemeEventSchema] was never
 * dispatched. Like [SetThemeEventSchema], this is a global override — not tied to screen
 * navigation.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("ResetTheme")
data class ResetThemeEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema