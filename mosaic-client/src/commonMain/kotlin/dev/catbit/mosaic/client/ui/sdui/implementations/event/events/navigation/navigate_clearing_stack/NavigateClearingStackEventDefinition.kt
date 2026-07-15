package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate_clearing_stack

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateClearingStackEventSchema

object NavigateClearingStackEventDefinition : EventDefinition<NavigateClearingStackEventSchema> {
    override val eventSchemaClass = NavigateClearingStackEventSchema::class
    override val eventRunner = NavigateClearingStackEventRunner
    override val eventHolderBuilder = NavigateClearingStackEventHolderBuilder
}
