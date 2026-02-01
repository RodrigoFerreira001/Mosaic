package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.client.ui.sdui.foundation.screen.DataHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenBehaviorsHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEditor
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEventDispatcher
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesOverlaysEditor
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.coroutines.supervisorScope
import org.koin.core.scope.Scope

class EventManager(
    private val screenId: String,
    private val eventRunnerManager: EventRunnerManager,
    private val koinScope: Scope
) {

    private val runningEvents = mutableMapOf<String, EventSchema>()

    private lateinit var tilesEditor: TilesEditor
    private lateinit var tilesOverlaysEditor: TilesOverlaysEditor
    private lateinit var tilesEventDispatcher: TilesEventDispatcher
    private lateinit var tilesEventHolder: TilesEventHolder
    private lateinit var screenBehaviorsHolder: ScreenBehaviorsHolder
    private lateinit var dataHolder: DataHolder

    fun attachTilesEditor(tilesEditor: TilesEditor) {
        this.tilesEditor = tilesEditor
    }

    fun attachTilesOverlaysEditor(tilesOverlaysEditor: TilesOverlaysEditor) {
        this.tilesOverlaysEditor = tilesOverlaysEditor
    }

    fun attachTilesEventHolder(tilesEventHolder: TilesEventHolder) {
        this.tilesEventHolder = tilesEventHolder
    }

    fun attachScreenBehaviors(screenBehaviorsHolder: ScreenBehaviorsHolder) {
        this.screenBehaviorsHolder = screenBehaviorsHolder
    }

    fun attachDataHolder(dataHolder: DataHolder) {
        this.dataHolder = dataHolder
    }

    fun attachTilesEventDispatcher(tilesEventDispatcher: TilesEventDispatcher) {
        this.tilesEventDispatcher = tilesEventDispatcher
    }

    suspend fun triggerEvents(
        trigger: EventTrigger,
        data: Any? = null
    ) {
        tilesEventHolder
            .getEventsByTrigger(trigger)
            ?.forEach { eventSchema ->
                supervisorScope {
                    runEvent(
                        eventSchema = eventSchema,
                        data = data
                    )
                }
            }
    }

    suspend fun onTrigger(
        eventOwnerId: String,
        trigger: EventTrigger,
        data: Any? = null
    ) {
        runningEvents[eventOwnerId]
            ?.events
            ?.filter { it.trigger == trigger }
            ?.forEach { eventSchema ->
                runEvent(
                    eventSchema = eventSchema,
                    data = data
                )
            }
    }

    suspend fun runEvents(
        eventSchemas: List<EventSchema>,
        data: Any? = null
    ) {
        eventSchemas.forEach { eventSchema ->
            runEvent(
                eventSchema = eventSchema,
                data = data
            )
        }
    }

    suspend fun runEvent(
        eventSchema: EventSchema,
        data: Any? = null
    ) {
        runningEvents[eventSchema.id] = eventSchema
        with(eventRunnerManager) {
            EventRunningScope(
                screenId = screenId,
                triggerOwnerId = eventSchema.id,
                incomingData = data,
                eventManager = this@EventManager,
                tilesEditor = tilesEditor,
                tilesOverlaysEditor = tilesOverlaysEditor,
                tilesEventDispatcher = tilesEventDispatcher,
                dataHolder = dataHolder,
                screenBehaviorsHolder = screenBehaviorsHolder,
                koinScope = koinScope
            ).runEvent(eventSchema)
        }
        runningEvents.remove(eventSchema.id)
    }
}