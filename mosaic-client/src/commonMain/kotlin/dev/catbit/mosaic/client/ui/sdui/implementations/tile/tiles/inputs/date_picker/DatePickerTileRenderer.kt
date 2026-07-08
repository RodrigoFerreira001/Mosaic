package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.date_picker

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.visible
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.epochMillisToIsoDate
import dev.catbit.mosaic.client.extensions.hasErrorState
import dev.catbit.mosaic.client.extensions.isoDateToEpochMillis
import dev.catbit.mosaic.client.extensions.textOrNull
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbol
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DatePickerTileSchema

@OptIn(ExperimentalMaterial3Api::class)
object DatePickerTileRenderer : TileRenderer<DatePickerTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: DatePickerTileSchema
    ) {
        with(tileSchema) {

            val modifier = Modifier
                .visible(isVisible())
                .styledWith(style)

            val interactionSource = remember { MutableInteractionSource() }

            LaunchedEffect(interactionSource, expanded) {
                interactionSource.interactions.collect { interaction ->
                    if (interaction is PressInteraction.Release) {
                        dispatchEvent(DatePickerTileEvents.OnDatePickerToggle)
                        triggerEvent(if (expanded) EventTriggers.onDatePickerClose() else EventTriggers.onDatePickerOpen())
                    }
                }
            }

            when (kind) {
                DatePickerTileSchema.Kind.FILLED -> {
                    TextField(
                        modifier = modifier,
                        value = selectedDate.orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        enabled = enabled,
                        isError = hasErrorState(),
                        supportingText = supportingText.textOrNull(),
                        interactionSource = interactionSource,
                        leadingIcon = {
                            MaterialSymbol("calendar_month")
                        }
                    )
                }

                DatePickerTileSchema.Kind.OUTLINED -> {
                    OutlinedTextField(
                        modifier = modifier,
                        value = selectedDate.orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        enabled = enabled,
                        isError = hasErrorState(),
                        supportingText = supportingText.textOrNull(),
                        interactionSource = interactionSource,
                        leadingIcon = {
                            MaterialSymbol("calendar_month")
                        }
                    )
                }
            }

            if (expanded) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = selectedDate?.let(::isoDateToEpochMillis)
                )

                DatePickerDialog(
                    onDismissRequest = {
                        dispatchEvent(DatePickerTileEvents.OnDatePickerDismissRequest)
                        triggerEvent(EventTriggers.onDatePickerClose())
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val iso = datePickerState.selectedDateMillis?.let(::epochMillisToIsoDate)
                                if (iso != null) {
                                    dispatchEvent(DatePickerTileEvents.OnDateConfirmed(iso))
                                    triggerEvent(trigger = EventTriggers.onDateSelected(), data = iso)
                                } else {
                                    dispatchEvent(DatePickerTileEvents.OnDatePickerDismissRequest)
                                }
                                triggerEvent(EventTriggers.onDatePickerClose())
                            }
                        ) {
                            Text(confirmLabel)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                dispatchEvent(DatePickerTileEvents.OnDatePickerDismissRequest)
                                triggerEvent(EventTriggers.onDatePickerClose())
                            }
                        ) {
                            Text(cancelLabel)
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}
