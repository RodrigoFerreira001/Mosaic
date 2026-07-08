package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.time_picker

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.visible
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.hasErrorState
import dev.catbit.mosaic.client.extensions.hourMinuteToIsoTime
import dev.catbit.mosaic.client.extensions.isoTimeToHourMinute
import dev.catbit.mosaic.client.extensions.textOrNull
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbol
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TimePickerTileSchema

@OptIn(ExperimentalMaterial3Api::class)
object TimePickerTileRenderer : TileRenderer<TimePickerTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: TimePickerTileSchema
    ) {
        with(tileSchema) {

            val modifier = Modifier
                .visible(isVisible())
                .styledWith(style)

            val interactionSource = remember { MutableInteractionSource() }

            LaunchedEffect(interactionSource, expanded) {
                interactionSource.interactions.collect { interaction ->
                    if (interaction is PressInteraction.Release) {
                        dispatchEvent(TimePickerTileEvents.OnTimePickerToggle)
                        triggerEvent(if (expanded) EventTriggers.onTimePickerClose() else EventTriggers.onTimePickerOpen())
                    }
                }
            }

            when (kind) {
                TimePickerTileSchema.Kind.FILLED -> {
                    TextField(
                        modifier = modifier,
                        value = selectedTime.orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        enabled = enabled,
                        isError = hasErrorState(),
                        supportingText = supportingText.textOrNull(),
                        interactionSource = interactionSource,
                        leadingIcon = {
                            MaterialSymbol("alarm")
                        }
                    )
                }

                TimePickerTileSchema.Kind.OUTLINED -> {
                    OutlinedTextField(
                        modifier = modifier,
                        value = selectedTime.orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        enabled = enabled,
                        isError = hasErrorState(),
                        supportingText = supportingText.textOrNull(),
                        interactionSource = interactionSource,
                        leadingIcon = {
                            MaterialSymbol("alarm")
                        }
                    )
                }
            }

            if (expanded) {
                val initialHourMinute = selectedTime?.let(::isoTimeToHourMinute)
                val timePickerState = rememberTimePickerState(
                    initialHour = initialHourMinute?.first ?: 0,
                    initialMinute = initialHourMinute?.second ?: 0,
                    is24Hour = true
                )

                TimePickerDialog(
                    title = {},
                    onDismissRequest = {
                        dispatchEvent(TimePickerTileEvents.OnTimePickerDismissRequest)
                        triggerEvent(EventTriggers.onTimePickerClose())
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val iso = hourMinuteToIsoTime(timePickerState.hour, timePickerState.minute)
                                dispatchEvent(TimePickerTileEvents.OnTimeConfirmed(iso))
                                triggerEvent(trigger = EventTriggers.onTimeSelected(), data = iso)
                                triggerEvent(EventTriggers.onTimePickerClose())
                            }
                        ) {
                            Text(confirmLabel)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                dispatchEvent(TimePickerTileEvents.OnTimePickerDismissRequest)
                                triggerEvent(EventTriggers.onTimePickerClose())
                            }
                        ) {
                            Text(cancelLabel)
                        }
                    }
                ) {
                    TimePicker(
                        state = timePickerState,
                        layoutType = TimePickerLayoutType.Vertical
                    )
                }
            }
        }
    }
}
