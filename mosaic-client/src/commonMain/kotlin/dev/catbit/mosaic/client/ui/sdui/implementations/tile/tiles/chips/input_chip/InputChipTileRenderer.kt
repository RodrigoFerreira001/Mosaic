package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.input_chip

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.iconOrNull
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.InputChipTileSchema

object InputChipTileRenderer : TileRenderer<InputChipTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: InputChipTileSchema,
    ) {
        with(tileSchema) {
            InputChip(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style),
                selected = selected,
                onClick = {
                    val newSelected = !selected
                    triggerEvent(if (newSelected) EventTriggers.onCheckEvent() else EventTriggers.onUncheck())
                    triggerEvent(EventTriggers.onCheckChanged())
                    dispatchEvent(InputChipTileEvents.OnCheckChanged(newSelected))
                },
                label = { Text(text) },
                enabled = enabled,
                leadingIcon = leadingIcon?.iconOrNull(),
                trailingIcon = trailingIcon?.iconOrNull(),
            )
        }
    }
}
