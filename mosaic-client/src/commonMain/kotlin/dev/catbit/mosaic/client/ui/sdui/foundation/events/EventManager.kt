package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenBehaviors
import dev.catbit.mosaic.client.ui.sdui.foundation.state.manager.TilesEditor
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.trigger.EventTrigger
import org.koin.core.scope.Scope

class EventManager(
    private val eventRunnerManager: EventRunnerManager,
    private val koinScope: Scope
) : EventRegister {

    private val events = mutableMapOf<String, List<EventModel>>()

    private lateinit var tilesEditor: TilesEditor
    private lateinit var screenBehaviors: ScreenBehaviors

    fun attachTilesEditor(tilesEditor: TilesEditor) {
        this.tilesEditor = tilesEditor
    }

    fun attachScreenBehaviors(screenBehaviors: ScreenBehaviors) {
        this.screenBehaviors = screenBehaviors
    }

    override fun registerEvents(
        eventOwnerId: String,
        eventList: List<EventModel>
    ) {
        events[eventOwnerId] = eventList
    }

    override fun unregisterEvents(
        eventOwnerId: String,
    ) {
        events.remove(eventOwnerId)
    }

    fun triggerEvents(
        trigger: EventTrigger,
        data: Any? = null
    ) {
        events
            .flatMap { it.value }
            .filter { it.trigger == trigger }
            .forEach { eventModel ->
                with(eventRunnerManager) {
                    EventRunningScope(
                        triggerOwnerId = eventModel.id,
                        incomingData = data,
                        eventManager = this@EventManager,
                        tilesEditor = tilesEditor,
                        screenBehaviors = screenBehaviors,
                        koinScope = koinScope
                    ).runEvent(eventModel)
                }
            }
    }

    fun triggerEvent(
        eventOwnerId: String,
        trigger: EventTrigger,
        data: Any? = null
    ) {
        events
            .getValue(eventOwnerId)
            .filter { it.trigger == trigger }
            .forEach { eventModel ->
                with(eventRunnerManager) {
                    EventRunningScope(
                        triggerOwnerId = eventModel.id,
                        incomingData = data,
                        eventManager = this@EventManager,
                        tilesEditor = tilesEditor,
                        screenBehaviors = screenBehaviors,
                        koinScope = koinScope
                    ).runEvent(eventModel)
                }
            }
    }

    fun runEvent(
        eventModel: EventModel,
        data: Any? = null
    ) {
        with(eventRunnerManager) {
            EventRunningScope(
                triggerOwnerId = eventModel.id,
                incomingData = data,
                eventManager = this@EventManager,
                tilesEditor = tilesEditor,
                screenBehaviors = screenBehaviors,
                koinScope = koinScope
            ).runEvent(eventModel)
        }
    }
}