package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.core.data.schemas.event.EventSchema

/**
 * Contract every Event — built-in or custom — implements to actually execute its own logic. Registered
 * against [T]'s class via an `EventDefinition`, and reached exclusively through
 * `EventManager.runEvent`, which builds the [EventRunningScope] this receives and wraps the whole
 * call in error handling — nothing else in the framework invokes an `EventRunner` directly.
 *
 * A stateless `object` is the norm (every built-in event runner is one). Work that should run off the
 * main thread wraps its body in `withContext(Dispatchers.IO) { }` directly, since [runEvent] is
 * already `suspend` — there's no separate scope-launching helper to reach for.
 */
interface EventRunner<out T : EventSchema> {
    /**
     * Executes [event]'s logic, ending by calling [EventRunningScope.onTrigger] (directly or, for a
     * runner whose real work can fail in several distinct ways, from inside a `runSafely`/`try-catch`
     * `onError` branch) with whichever outcome trigger applies — `onSuccess()`/`onFailure()` at
     * minimum, plus whatever narrower triggers (`onStart()`, status-specific ones, cancellation, etc.)
     * this event's own catalog entry declares.
     *
     * @receiver [EventRunningScope], carrying this event's own `incomingData` and every collaborator
     * needed to read/write data, edit the tile tree, run further events, or resolve dependencies.
     * @param event the current schema instance to execute — the same value [triggerOwner]
     * ([EventRunningScope.triggerOwner]) already points to, passed again as a parameter for direct,
     * type-safe access to [T]'s own fields.
     */
    suspend fun EventRunningScope.runEvent(event: @UnsafeVariance T)
}