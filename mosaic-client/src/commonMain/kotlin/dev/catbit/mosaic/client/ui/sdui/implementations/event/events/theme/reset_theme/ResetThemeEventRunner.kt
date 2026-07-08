package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.theme.reset_theme

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.theme.MosaicColors
import dev.catbit.mosaic.core.data.schemas.event.events.theme.ResetThemeEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object ResetThemeEventRunner : EventRunner<ResetThemeEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: ResetThemeEventSchema) {
        get<MosaicColors>().resetColorScheme()
        onTrigger(EventTriggers.onSuccess())
    }
}
