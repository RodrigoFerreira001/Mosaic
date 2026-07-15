package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.image.async_image

import androidx.compose.foundation.layout.visible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import dev.catbit.mosaic.client.extensions.toAlignment
import dev.catbit.mosaic.client.extensions.toContentScale
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.AsyncImageTileSchema
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object AsyncImageTileRenderer : TileRenderer<AsyncImageTileSchema> {

    @OptIn(ExperimentalEncodingApi::class)
    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: AsyncImageTileSchema,
    ) {
        with(tileSchema) {

            val model = remember(model) {
                when (val m = model) {
                    is AsyncImageTileSchema.Model.Url -> m.url
                    is AsyncImageTileSchema.Model.ArrayOfBytes -> m.byteArray
                    is AsyncImageTileSchema.Model.Base64 -> Base64.decode(m.base64)
                }
            }

            AsyncImage(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style),
                onLoading = { triggerEvent(EventTriggers.onAsyncImageLoadStart()) },
                onError = { triggerEvent(EventTriggers.onAsyncImageLoadFailure()) },
                onSuccess = { triggerEvent(EventTriggers.onAsyncImageLoadSuccess()) },
                model = model,
                contentDescription = contentDescription,
                contentScale = contentScale.toContentScale(),
                alpha = alpha,
                clipToBounds = clipToBounds,
                alignment = alignment.toAlignment()
            )
        }
    }
}
