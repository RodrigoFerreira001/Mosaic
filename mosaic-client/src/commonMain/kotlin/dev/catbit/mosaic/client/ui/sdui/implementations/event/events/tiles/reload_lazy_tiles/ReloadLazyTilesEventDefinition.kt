package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.reload_lazy_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReloadLazyTilesEventSchema

object ReloadLazyTilesEventDefinition : EventDefinition<ReloadLazyTilesEventSchema> {
    override val eventSchemaClass = ReloadLazyTilesEventSchema::class
    override val eventRunner = ReloadLazyTilesEventRunner
    override val eventHolderBuilder = ReloadLazyTilesEventHolderBuilder
}
