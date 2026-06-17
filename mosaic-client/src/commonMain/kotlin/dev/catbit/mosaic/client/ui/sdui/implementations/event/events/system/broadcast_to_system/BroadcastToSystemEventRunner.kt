package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.broadcast_to_system

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.foundation.system_broadcast.SystemBroadcastChannel
import dev.catbit.mosaic.core.data.schemas.event.events.system.BroadcastToSystemEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object BroadcastToSystemEventRunner : EventRunner<BroadcastToSystemEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: BroadcastToSystemEventSchema) {
        with(event) {
            when (data) {
                BroadcastToSystemEventSchema.BroadcastData.Incoming -> {
                    incomingData?.let {
                        get<SystemBroadcastChannel>().broadcast(
                            broadcastId = broadcastId,
                            data = incomingData
                        )
                        onTrigger(EventTriggers.onSuccess())
                    } ?: run {
                        onTrigger(EventTriggers.onFailure())
                    }
                }

                is BroadcastToSystemEventSchema.BroadcastData.Inline -> {
                    get<SystemBroadcastChannel>().broadcast(
                        broadcastId = broadcastId,
                        data = (data as BroadcastToSystemEventSchema.BroadcastData.Inline).data
                    )
                    onTrigger(EventTriggers.onSuccess())
                }
            }
        }
    }
}
