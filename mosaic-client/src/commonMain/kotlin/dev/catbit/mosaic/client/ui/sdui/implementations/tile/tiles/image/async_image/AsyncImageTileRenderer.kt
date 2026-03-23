package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.image.async_image

import androidx.compose.foundation.layout.visible
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import dev.catbit.mosaic.client.extensions.toAlignment
import dev.catbit.mosaic.client.extensions.toContentScale
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.AsyncImageTileSchema

object AsyncImageTileRenderer : TileRenderer<AsyncImageTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: AsyncImageTileSchema,
    ) {
        with(tileSchema) {
            AsyncImage(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style),
                onLoading = { triggerEvent(EventTriggers.onAsyncImageLoadStart()) },
                onError = { triggerEvent(EventTriggers.onAsyncImageLoadFailure()) },
                onSuccess = { triggerEvent(EventTriggers.onAsyncImageLoadSuccess()) },
                model = url,
                contentDescription = contentDescription,
                contentScale = contentScale.toContentScale(),
                alpha = alpha,
                clipToBounds = clipToBounds,
                alignment = alignment.toAlignment()
            )
        }
    }
}
