package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.get_tile_children_count

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.GetTileChildrenCountEventSchema

object GetTileChildrenCountEventDefinition : EventDefinition<GetTileChildrenCountEventSchema> {
    override val eventSchemaClass = GetTileChildrenCountEventSchema::class
    override val eventRunner = GetTileChildrenCountEventRunner
    override val eventHolderBuilder = GetTileChildrenCountEventHolderBuilder
}
