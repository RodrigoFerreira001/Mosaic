package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.security.request_permission

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.security.RequestPermissionEventModel

object RequestPermissionEventDefinition : EventDefinition<RequestPermissionEventModel> {
    override val eventModelClass = RequestPermissionEventModel::class
    override val eventRunner = RequestPermissionEventRunner
    override val eventHolderBuilder = RequestPermissionEventHolderBuilder
}
