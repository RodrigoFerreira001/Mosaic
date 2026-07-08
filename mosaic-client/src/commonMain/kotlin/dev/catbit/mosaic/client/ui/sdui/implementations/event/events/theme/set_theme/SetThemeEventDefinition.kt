package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.theme.set_theme

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.theme.SetThemeEventSchema

object SetThemeEventDefinition : EventDefinition<SetThemeEventSchema> {
    override val eventSchemaClass = SetThemeEventSchema::class
    override val eventRunner = SetThemeEventRunner
    override val eventHolderBuilder = SetThemeEventHolderBuilder
}
