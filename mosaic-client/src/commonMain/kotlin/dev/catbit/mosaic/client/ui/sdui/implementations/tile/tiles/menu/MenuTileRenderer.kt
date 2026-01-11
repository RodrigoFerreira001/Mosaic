package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.composables.icon.Icon
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.event_trigger.EventTriggers

object MenuTileRenderer : TileRenderer<MenuTileUIState> {

    @Composable
    override fun TileRenderingScope.Render(uiState: MenuTileUIState) {
        Box {
            RenderChildren(uiState.tiles)

            DropdownMenu(
                expanded = uiState.expanded,
                onDismissRequest = { dispatchEvent(MenuTileEvent.OnToggleMenu) }
            ) {
                uiState.items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(item.label)
                        },
                        onClick = {
                            triggerEvent(EventTriggers.onMenuItemClick(item.id))
                        },
                        leadingIcon = item.leadingIcon?.let { icon ->
                            { Icon(icon) }
                        },
                        trailingIcon = item.trailingIcon?.let { icon ->
                            { Icon(icon) }
                        }
                    )
                }
            }
        }
    }
}