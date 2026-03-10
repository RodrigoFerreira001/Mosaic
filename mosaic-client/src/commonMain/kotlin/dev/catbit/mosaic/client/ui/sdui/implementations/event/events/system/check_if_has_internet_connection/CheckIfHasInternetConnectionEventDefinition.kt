package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.check_if_has_internet_connection

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.system.CheckIfHasInternetConnectionEventSchema

object CheckIfHasInternetConnectionEventDefinition : EventDefinition<CheckIfHasInternetConnectionEventSchema> {
    override val eventSchemaClass = CheckIfHasInternetConnectionEventSchema::class
    override val eventRunner = CheckIfHasInternetConnectionEventRunner
    override val eventHolderBuilder = CheckIfHasInternetConnectionEventHolderBuilder
}
