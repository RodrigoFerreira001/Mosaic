package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.row

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.row.ScrollRowTileEventSchema

object ScrollRowTileEventRunner : EventRunner<ScrollRowTileEventSchema> {
    override fun EventRunningScope.runEvent(event: ScrollRowTileEventSchema) {
        println("Running ScrollRowTileEvent: $event")
    }
}
