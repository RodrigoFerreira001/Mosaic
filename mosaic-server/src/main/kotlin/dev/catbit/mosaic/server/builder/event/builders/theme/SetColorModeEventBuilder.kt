package dev.catbit.mosaic.server.builder.event.builders.theme

import dev.catbit.mosaic.core.data.schemas.event.events.theme.SetColorModeEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class SetColorModeEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val colorMode: SetColorModeEventSchema.ColorMode
) : EventSchemaBuilder<SetColorModeEventSchema>() {

    override fun build() = SetColorModeEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        colorMode = colorMode
    )
}

/**
 * Sets the app's color mode — light, dark, or following the system setting — overriding whatever
 * was set before. Unlike `SetTheme`, which swaps the Material color roles themselves, this only
 * decides which of the already-installed light/dark schemes is active. Does not consume
 * `incomingData`. Dispatches `onSuccess` (no data) always, after the color mode is applied.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its trigger (`onSuccess`).
 * @param colorMode Color mode to apply — [colorModeLight], [colorModeDark] or [colorModeSystemDefault].
 */
fun EventSchemaBuilderScope.SetColorMode(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    colorMode: SetColorModeEventSchema.ColorMode = colorModeSystemDefault()
) {
    addBuilder(
        SetColorModeEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            colorMode = colorMode
        )
    )
}

/** Always renders with the light color scheme, regardless of the system setting. */
fun colorModeLight() = SetColorModeEventSchema.ColorMode.Light

/** Always renders with the dark color scheme, regardless of the system setting. */
fun colorModeDark() = SetColorModeEventSchema.ColorMode.Dark

/** Follows the system's light/dark setting. */
fun colorModeSystemDefault() = SetColorModeEventSchema.ColorMode.SystemDefault
