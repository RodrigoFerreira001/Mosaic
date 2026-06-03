package dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar

import dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar.DisplaySnackbarEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar.SnackbarDurationSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DisplaySnackbarEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val message: String,
    private val duration: SnackbarDurationSchema,
    private val actionLabel: String?
) : EventSchemaBuilder<DisplaySnackbarEventSchema>() {

    override fun build() = DisplaySnackbarEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        message = message,
        duration = duration,
        actionLabel = actionLabel
    )
}

fun EventSchemaBuilderScope.DisplaySnackbar(
    id: String = randomId(),
    trigger: EventTrigger,
    message: String,
    duration: SnackbarDurationSchema = SnackbarDurationSchema.Short,
    actionLabel: String? = null,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        DisplaySnackbarEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            message = message,
            duration = duration,
            actionLabel = actionLabel
        )
    )
}
