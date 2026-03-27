package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.navigation_bar

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.textOrNull
import dev.catbit.mosaic.client.ui.composables.icon.Icon
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationBarTileSchema

object NavigationBarTileRenderer : TileRenderer<NavigationBarTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: NavigationBarTileSchema,
    ) {
        with(tileSchema) {
            NavigationBar(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style)
            ) {
                items.forEach { navigationBarItem ->
                    with(navigationBarItem) {
                        NavigationBarItem(
                            selected = selectedItemId == id,
                            onClick = {
                                triggerEvent(EventTriggers.onNavigationBarItemClick(id))
                                dispatchEvent(NavigationBarTileEvents.OnItemClicked(id))
                            },
                            icon = {
                                Icon(
                                    schema = icon,
                                    filled = selectedItemId == id
                                )
                            },
                            label = label.textOrNull()
                        )
                    }
                }
            }
        }
    }
}
