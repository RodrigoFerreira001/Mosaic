package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.theme.set_color_mode

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.theme.MosaicColorMode
import dev.catbit.mosaic.core.data.schemas.event.events.theme.SetColorModeEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object SetColorModeEventRunner : EventRunner<SetColorModeEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: SetColorModeEventSchema) {
        val colorMode = when (event.colorMode) {
            SetColorModeEventSchema.ColorMode.Light -> MosaicColorMode.ColorMode.LIGHT
            SetColorModeEventSchema.ColorMode.Dark -> MosaicColorMode.ColorMode.DARK
            SetColorModeEventSchema.ColorMode.SystemDefault -> MosaicColorMode.ColorMode.SYSTEM_DEFAULT
        }

        get<MosaicColorMode>().setMode(colorMode)
        onTrigger(EventTriggers.onSuccess())
    }
}
