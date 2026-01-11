package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.client.ui.sdui.foundation.screen.DataHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenBehaviorsHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.manager.TilesEditor
import dev.catbit.mosaic.client.ui.sdui.foundation.state.manager.TilesEventDispatcher
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import org.koin.core.scope.Scope

class EventManager(
    private val eventRunnerManager: EventRunnerManager,
    private val koinScope: Scope
) : EventRegister {

    private val events = mutableMapOf<String, List<EventModel>>()

    private lateinit var tilesEditor: TilesEditor
    private lateinit var tilesEventDispatcher: TilesEventDispatcher
    private lateinit var screenBehaviorsHolder: ScreenBehaviorsHolder
    private lateinit var dataHolder: DataHolder

    fun attachTilesEditor(tilesEditor: TilesEditor) {
        this.tilesEditor = tilesEditor
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

    suspend fun triggerEvents(
        trigger: EventTrigger,
        data: Any? = null
    ) {
        events
            .flatMap { it.value }
            .filter { it.trigger == trigger }
            .forEach { eventModel ->
                runEvent(
                    eventModel = eventModel,
                    data = data
                )
            }
    }

    suspend fun triggerEvent(
        eventOwnerId: String,
        trigger: EventTrigger,
        data: Any? = null
    ) {
        events[eventOwnerId]
            ?.filter { it.trigger == trigger }
            ?.forEach { eventModel ->
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
        eventModel.events?.let {
            events[eventModel.id] = it
        }
        with(eventRunnerManager) {
            EventRunningScope(
                triggerOwnerId = eventModel.id,
                incomingData = data,
                eventManager = this@EventManager,
                tilesEditor = tilesEditor,
                tilesEventDispatcher = tilesEventDispatcher,
                dataHolder = dataHolder,
                screenBehaviorsHolder = screenBehaviorsHolder,
                koinScope = koinScope
            ).runEvent(eventModel)
        }
        events.remove(eventModel.id)
    }
}