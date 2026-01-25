package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.composables.icon.Icon
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.event_trigger.EventTriggers
import dev.catbit.mosaic.core.data.tile.tiles.menu.MenuTileModel

object MenuTileRenderer : TileRenderer<MenuTileModel> {

    @Composable
    override fun TileRenderingScope.Render(tileModel: MenuTileModel) {
        with(tileModel) {
            Box {
                RenderChildren(tiles)

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { dispatchEvent(MenuTileEvents.OnToggleMenu) }
                ) {
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(item.label)
                            },
                            onClick = {
                                triggerEvent(EventTriggers.onMenuItemClick(item.id))
                            },
                            leadingIcon = item.leadingIcon?.let { icon ->
                                { Icon(icon.name) }
                            },
                            trailingIcon = item.trailingIcon?.let { icon ->
                                { Icon(icon.name) }
                            }
                        )
                    }
                }
            }
        }
    }
}