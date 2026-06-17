package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.snackbar.display

import androidx.compose.material3.SnackbarDuration
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileScreenTilesBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar.DisplaySnackbarEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar.SnackbarDurationSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object DisplaySnackbarEventRunner : EventRunner<DisplaySnackbarEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: DisplaySnackbarEventSchema) {

        val duration = when (event.duration) {
            SnackbarDurationSchema.Short -> SnackbarDuration.Short
            SnackbarDurationSchema.Long -> SnackbarDuration.Long
            SnackbarDurationSchema.Indefinite -> SnackbarDuration.Indefinite
        }

        broadcastData(
            ScreenTileScreenTilesBroadcastData.DisplaySnackbar(
                message = event.message,
                duration = duration,
                actionLabel = event.actionLabel,
                onAction = {
                    onTrigger(EventTriggers.onSnackbarAction())
                },
                onDismiss = {
                    onTrigger(EventTriggers.onSnackbarDismissed())
                }
            )
        )
        onTrigger(EventTriggers.onSuccess())
    }
}
