package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.client.ui.sdui.foundation.screen.DataHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenBehaviorsHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.manager.TilesEditor
import dev.catbit.mosaic.client.ui.sdui.foundation.state.manager.TilesEventDispatcher
import dev.catbit.mosaic.client.ui.sdui.foundation.state.manager.TilesEventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.manager.TilesOverlaysEditor
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import kotlinx.coroutines.supervisorScope
import org.koin.core.scope.Scope

class EventManager(
    private val eventRunnerManager: EventRunnerManager,
    private val koinScope: Scope
) {

    private val runningEvents = mutableMapOf<String, EventModel>()

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
            ?.forEach { eventModel ->
                supervisorScope {
                    runEvent(
                        eventModel = eventModel,
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
            ?.forEach { eventModel ->
                runEvent(
                    eventModel = eventModel,
                    data = data
                )
            }
    }

    suspend fun runEvents(
        eventModels: List<EventModel>,
        data: Any? = null
    ) {
        eventModels.forEach { eventModel ->
            runEvent(
                eventModel = eventModel,
                data = data
            )
        }
    }

    suspend fun runEvent(
        eventModel: EventModel,
        data: Any? = null
    ) {
        runningEvents[eventModel.id] = eventModel
        with(eventRunnerManager) {
            EventRunningScope(
                triggerOwnerId = eventModel.id,
                incomingData = data,
                eventManager = this@EventManager,
                tilesEditor = tilesEditor,
                tilesOverlaysEditor = tilesOverlaysEditor,
                tilesEventDispatcher = tilesEventDispatcher,
                dataHolder = dataHolder,
                screenBehaviorsHolder = screenBehaviorsHolder,
                koinScope = koinScope
            ).runEvent(eventModel)
        }
        runningEvents.remove(eventModel.id)
    }
}