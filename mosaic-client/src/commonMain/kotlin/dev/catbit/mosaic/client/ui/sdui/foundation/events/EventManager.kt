package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.trigger.EventTrigger
import org.koin.core.scope.Scope

class EventManager(
    private val eventRunnerManager: EventRunnerManager,
    val koinScope: Scope
) {

    private val events = mutableMapOf<String, List<EventModel>>()

    fun registerEvents(
        eventOwnerId: String,
        eventList: List<EventModel>
    ) {
        events[eventOwnerId] = eventList
    }

    fun unregisterEvents(
        eventOwnerId: String,
    ) {
        events.remove(eventOwnerId)
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
                        onTrigger = { trigger, data ->
                            eventModel
                                .events
                                ?.filter { it.trigger == trigger }
                                ?.forEach { runEvent(it, data) }
                        },
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
                onTrigger = { trigger, data ->
                    eventModel
                        .events
                        ?.filter { it.trigger == trigger }
                        ?.forEach { runEvent(it, data) }
                },
                koinScope = koinScope
            ).runEvent(eventModel)
        }
    }
}