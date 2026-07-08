package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.theme.reset_theme

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.theme.ResetThemeEventSchema

object ResetThemeEventDefinition : EventDefinition<ResetThemeEventSchema> {
    override val eventSchemaClass = ResetThemeEventSchema::class
    override val eventRunner = ResetThemeEventRunner
    override val eventHolderBuilder = ResetThemeEventHolderBuilder
}
