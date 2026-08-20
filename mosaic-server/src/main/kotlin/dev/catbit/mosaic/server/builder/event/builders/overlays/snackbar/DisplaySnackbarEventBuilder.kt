package dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar

import dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar.DisplaySnackbarEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DisplaySnackbarEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val message: String,
    private val duration: DisplaySnackbarEventSchema.SnackbarDuration,
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

/**
 * Shows a snackbar with [message], by broadcasting a display command on the screen channel.
 * [duration] maps onto Material's short/long/indefinite durations, and [actionLabel] adds an
 * action button when non-null. Does not consume `incomingData`. Dispatches `onSuccess` (no data)
 * right after the command is broadcast, before the snackbar has resolved — the broadcast is
 * fire-and-forget, so this fires regardless of what the snackbar does next; later,
 * `onSnackbarAction` (no data) when the user presses the action button, or
 * `onSnackbarDismissed` (no data) when the snackbar goes away without its action being pressed.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param message Text shown in the snackbar.
 * @param duration How long the snackbar stays visible — [snackbarShortDuration], [snackbarLongDuration] or [snackbarIndefiniteDuration]. Defaults to short.
 * @param actionLabel Label of the snackbar's action button. Defaults to none (no action button).
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onSnackbarAction`, `onSnackbarDismissed`).
 */
fun EventSchemaBuilderScope.DisplaySnackbar(
    id: String = randomId(),
    trigger: EventTrigger,
    message: String,
    duration: DisplaySnackbarEventSchema.SnackbarDuration = snackbarShortDuration(),
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

/** Snackbar stays visible for Material's short duration. */
fun snackbarShortDuration() = DisplaySnackbarEventSchema.SnackbarDuration.Short

/** Snackbar stays visible for Material's long duration. */
fun snackbarLongDuration() = DisplaySnackbarEventSchema.SnackbarDuration.Long

/** Snackbar stays visible until dismissed by its action or manually — never times out on its own. */
fun snackbarIndefiniteDuration() = DisplaySnackbarEventSchema.SnackbarDuration.Indefinite
