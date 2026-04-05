package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.navigation_rail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.visible
import androidx.compose.material3.ModalWideNavigationRail
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.textOrNull
import dev.catbit.mosaic.client.ui.composables.icon.Icon
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationRailTileSchema

object NavigationRailTileRenderer : TileRenderer<NavigationRailTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: NavigationRailTileSchema,
    ) {
        with(tileSchema) {
            NavigationRail(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style),
                header = header?.let { header ->
                    @Composable {
                        RenderChild(header)
                    }
                }
            ) {
                items.forEach { navigationRailItem ->
                    with(navigationRailItem) {
                        NavigationRailItem(
                            selected = selectedItemId == id,
                            onClick = {
                                triggerEvent(EventTriggers.onNavigationRailItemClick(id))
                                dispatchEvent(NavigationRailTileEvents.OnItemClicked(id))
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

                footer?.let {
                    Spacer(Modifier.weight(1f))
                    RenderChild(it)
                }
            }
        }
    }
}
