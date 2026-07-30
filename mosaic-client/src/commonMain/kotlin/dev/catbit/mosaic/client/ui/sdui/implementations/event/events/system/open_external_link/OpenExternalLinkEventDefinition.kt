package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.open_external_link

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.system.OpenExternalLinkEventSchema

object OpenExternalLinkEventDefinition : EventDefinition<OpenExternalLinkEventSchema> {
    override val eventSchemaClass = OpenExternalLinkEventSchema::class
    override val eventRunner = OpenExternalLinkEventRunner
    override val eventHolderBuilder = OpenExternalLinkEventHolderBuilder
}
