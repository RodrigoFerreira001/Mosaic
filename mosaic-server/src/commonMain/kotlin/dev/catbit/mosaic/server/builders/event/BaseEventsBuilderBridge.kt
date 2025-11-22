package dev.catbit.mosaic.server.builders.event

import dev.catbit.mosaic.server.annotations.BuildFor
import dev.catbit.mosaic.core.data.event.events.NavigateEventModel
import dev.catbit.mosaic.core.data.event.events.SendNetworkRequestEventModel

@BuildFor(
    [
        NavigateEventModel::class,
        SendNetworkRequestEventModel::class,
    ]
)
internal object BaseEventsBuilderBridge