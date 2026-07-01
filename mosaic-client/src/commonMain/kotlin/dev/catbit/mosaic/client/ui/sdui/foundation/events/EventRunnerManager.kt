package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.client.logger.Level
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import kotlin.reflect.KClass

class EventRunnerManager(
    private val eventRunners: Map<KClass<out EventSchema>, EventRunner<*>>
) {
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
            println("Couldn't find a runner for $event") // TODO Usar https://github.com/touchlab/Kermit
        }
    }
}