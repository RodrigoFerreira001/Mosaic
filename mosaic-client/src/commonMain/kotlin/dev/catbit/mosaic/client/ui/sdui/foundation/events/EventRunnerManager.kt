package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.core.data.event.EventModel
import kotlin.reflect.KClass

class EventRunnerManager(
    private val eventRunners: Map<KClass<out EventModel>, EventRunner<*>>
) {
    fun EventRunningScope.runEvent(event: EventModel) {
        eventRunners[event::class]?.let { runner ->
            with(runner) {
                runEvent(event)
            }
        } ?: run {
            println("Couldn't find a runner for $event") // TODO Usar logger
        }
    }
}