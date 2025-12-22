package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.core.trigger.EventTrigger
import org.koin.core.scope.Scope

data class EventRunningScope(
    val triggerOwnerId: String,
    val incomingData: Any? = null,
    val onTrigger: (trigger: EventTrigger, data: Any?) -> Unit,
    val koinScope: Scope, // TODO vou precisar de manipular a UI, receber aqui alguma UIBridge sei lá
) {
    fun triggerEvent(
        eventTrigger: EventTrigger,
        data: Any? = null
    ) {
        onTrigger(eventTrigger, data)
    }
}