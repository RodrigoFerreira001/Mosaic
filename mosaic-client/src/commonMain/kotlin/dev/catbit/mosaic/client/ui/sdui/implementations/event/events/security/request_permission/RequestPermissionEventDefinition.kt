package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.security.request_permission

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema

object RequestPermissionEventDefinition : EventDefinition<RequestPermissionEventSchema> {
    override val eventSchemaClass = RequestPermissionEventSchema::class
    override val eventRunner = RequestPermissionEventRunner
    override val eventHolderBuilder = RequestPermissionEventHolderBuilder
}
