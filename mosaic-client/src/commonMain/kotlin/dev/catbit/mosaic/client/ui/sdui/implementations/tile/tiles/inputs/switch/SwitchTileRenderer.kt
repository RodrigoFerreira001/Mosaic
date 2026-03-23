package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.switch

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.SwitchTileSchema

object SwitchTileRenderer : TileRenderer<SwitchTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: SwitchTileSchema,
    ) {
        with(tileSchema) {
            Switch(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style),
                enabled = enabled,
                checked = checked,
                onCheckedChange = { checked ->
                    triggerEvent(if (checked) EventTriggers.onCheckEvent() else EventTriggers.onUncheck())
                    triggerEvent(EventTriggers.onCheckChanged())
                    dispatchEvent(SwitchTileEvents.OnCheckChanged(checked))
                }
            )
        }
    }
}
