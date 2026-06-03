package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.image.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.visible
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.toAlignment
import dev.catbit.mosaic.client.extensions.toContentScale
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.resources.DrawableResourcesHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.ImageTileSchema
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

object ImageTileRenderer : TileRenderer<ImageTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: ImageTileSchema,
    ) {
        with(tileSchema) {
            koinInject<DrawableResourcesHolder>()[resourceName]?.let {
                Image(
                    modifier = Modifier
                        .visible(isVisible())
                        .styledWith(style),
                    painter = painterResource(it),
                    contentDescription = contentDescription,
                    contentScale = contentScale.toContentScale(),
                    alpha = alpha,
                    alignment = alignment.toAlignment()
                )
            }
        }
    }
}
