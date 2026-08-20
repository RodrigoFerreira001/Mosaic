package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.client.logger.Level
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import kotlin.reflect.KClass

/**
 * Resolves an [EventSchema]'s concrete class to its registered [EventRunner] and actually runs it —
 * the single entry point every event, built-in or custom, goes through, reached via
 * `EventManager.runEvent`.
 *
 * One instance is built app-wide (by `MosaicModules`), from the merge of every built-in
 * `EventDefinition` and whatever custom ones were passed via
 * `MosaicDependencyInjectionConfig.eventDefinitions` — so a custom event runs through exactly the
 * same path as a built-in one.
 *
 * @param eventRunners every registered [EventRunner], keyed by the [EventSchema] subclass it runs.
 */
class EventRunnerManager(
    private val eventRunners: Map<KClass<out EventSchema>, EventRunner<*>>
) {
    /**
     * Runs [event] by looking up the [EventRunner] registered for its concrete class and invoking
     * its `runEvent`. If no runner is registered for [event]'s class at all — the schema's own class
     * was never included in `MosaicDependencyInjectionConfig.eventDefinitions` — nothing runs and an
     * error is logged instead of throwing.
     *
     * @receiver [EventRunningScope] this event runs with.
     * @param event the event to run.
     */
    suspend fun EventRunningScope.runEvent(event: EventSchema) {
        eventRunners[event::class]?.let { runner ->
            with(runner) {
                log(
                    level = Level.DEBUG,
                    msg = """
                        #===============>
                        Running event ${event::class.simpleName}#${event.id}
                        Payload: $event
                        <===============#
                    """.trimIndent()
                )
                runEvent(event)
            }
        } ?: run {
            log(Level.ERROR, "EventRunnerManager: Couldn't find a runner for $event")
        }
    }
}