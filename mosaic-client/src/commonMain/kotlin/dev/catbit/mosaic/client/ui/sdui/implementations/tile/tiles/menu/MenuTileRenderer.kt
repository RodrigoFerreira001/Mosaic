package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.ui.composables.icon.Icon
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.menu.MenuTileSchema

object MenuTileRenderer : TileRenderer<MenuTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(tileSchema: MenuTileSchema) {
        with(tileSchema) {
            Box {
                RenderChildren(tiles)

                DropdownMenu(
                    modifier = Modifier
                        .systemBarsPadding()
                        .heightIn(max = 400.dp),
                    expanded = expanded,
                    shape = MaterialTheme.shapes.large,
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
                                { Icon(schema = icon) }
                            },
                            trailingIcon = item.trailingIcon?.let { icon ->
                                { Icon(schema = icon) }
                            }
                        )
                    }
                }
            }
        }
    }
}