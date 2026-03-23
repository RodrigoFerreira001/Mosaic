package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.radio_button

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.RadioButtonTileSchema

object RadioButtonTileRenderer : TileRenderer<RadioButtonTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: RadioButtonTileSchema,
    ) {
        with(tileSchema) {
            RadioButton(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style),
                selected = selected,
                enabled = enabled,
                onClick = {
                    triggerEvent(EventTriggers.onSelect())
                    dispatchGroupEvent(
                        RadioButtonTileGroupEvents.OnRadioSelected(
                            selectedTileId = id,
                            groupId = groupId
                        )
                    )
                }
            )
        }
    }
}
