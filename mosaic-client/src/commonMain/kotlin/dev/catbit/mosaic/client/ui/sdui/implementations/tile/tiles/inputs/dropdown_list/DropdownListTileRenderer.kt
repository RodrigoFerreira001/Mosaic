package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.dropdown_list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.visible
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.hasErrorState
import dev.catbit.mosaic.client.extensions.textOrNull
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DropdownListTileSchema

@OptIn(ExperimentalMaterial3Api::class)
object DropdownListTileRenderer : TileRenderer<DropdownListTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: DropdownListTileSchema
    ) {
        with(tileSchema) {

            val modifier = Modifier
                .visible(isVisible())
                .styledWith(style)

            val selectedOption = options.first { it.id == selectedOptionId }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    dispatchEvent(DropdownListTileEvents.OnDropdownListToggle)
                    triggerEvent(if (expanded) EventTriggers.onDropdownListClose() else EventTriggers.onDropdownListOpen())
                }
            ) {
                when (kind) {
                    DropdownListTileSchema.Kind.FILLED -> {
                        TextField(
                            modifier = modifier.menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                enabled = enabled
                            ),
                            value = selectedOption.label,
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            onValueChange = {},
                            readOnly = true,
                            isError = hasErrorState(),
                            supportingText = supportingText.textOrNull(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                        )
                    }

                    DropdownListTileSchema.Kind.OUTLINED -> {
                        OutlinedTextField(
                            modifier = modifier.menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                enabled = enabled
                            ),
                            value = selectedOption.label,
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            onValueChange = {},
                            readOnly = true,
                            isError = hasErrorState(),
                            supportingText = supportingText.textOrNull(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                        )
                    }
                }

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        dispatchEvent(DropdownListTileEvents.OnDropdownListDismissRequest)
                        triggerEvent(EventTriggers.onDropdownListClose())
                    }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label) },
                            onClick = {
                                dispatchEvent(DropdownListTileEvents.OnItemSelected(option.id))
                                triggerEvent(EventTriggers.onDropdownListItemSelected(option.id))
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
