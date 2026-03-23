package dev.catbit.mosaic.client.extensions

import androidx.compose.ui.layout.ContentScale
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.AsyncImageTileSchema.ContentScale as ContentScaleSchema

fun ContentScaleSchema.toContentScale() = when (this) {
    ContentScaleSchema.CROP -> ContentScale.Crop
    ContentScaleSchema.FIT -> ContentScale.Fit
    ContentScaleSchema.FILL_HEIGHT -> ContentScale.FillHeight
    ContentScaleSchema.FILL_WIDTH -> ContentScale.FillWidth
    ContentScaleSchema.INSIDE -> ContentScale.Inside
    ContentScaleSchema.FILL_BOUNDS -> ContentScale.FillBounds
}