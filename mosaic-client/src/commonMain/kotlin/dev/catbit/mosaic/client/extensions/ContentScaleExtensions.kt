package dev.catbit.mosaic.client.extensions

import androidx.compose.ui.layout.ContentScale
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.AsyncImageTileSchema.ContentScale as AsyncContentScaleSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.ImageTileSchema.ContentScale as ImageContentScaleSchema

/** Converts `AsyncImage`'s own nested `ContentScale` enum into its Compose [ContentScale]
 * counterpart. A distinct type from [ImageContentScaleSchema.toContentScale] below despite the
 * identical set of cases — `AsyncImage` and `Image` each declare their own `ContentScale` type on
 * the wire, so their DSL helpers (`cropContentScale()` vs `imageCropContentScale()`, etc.) aren't
 * interchangeable either. */
fun AsyncContentScaleSchema.toContentScale() = when (this) {
    AsyncContentScaleSchema.CROP -> ContentScale.Crop
    AsyncContentScaleSchema.FIT -> ContentScale.Fit
    AsyncContentScaleSchema.FILL_HEIGHT -> ContentScale.FillHeight
    AsyncContentScaleSchema.FILL_WIDTH -> ContentScale.FillWidth
    AsyncContentScaleSchema.INSIDE -> ContentScale.Inside
    AsyncContentScaleSchema.FILL_BOUNDS -> ContentScale.FillBounds
}

/** Converts `Image`'s own nested `ContentScale` enum into its Compose [ContentScale] counterpart —
 * see [AsyncContentScaleSchema.toContentScale] above for why this is a separate function rather than
 * a shared one. */
fun ImageContentScaleSchema.toContentScale() = when (this) {
    ImageContentScaleSchema.CROP -> ContentScale.Crop
    ImageContentScaleSchema.FIT -> ContentScale.Fit
    ImageContentScaleSchema.FILL_HEIGHT -> ContentScale.FillHeight
    ImageContentScaleSchema.FILL_WIDTH -> ContentScale.FillWidth
    ImageContentScaleSchema.INSIDE -> ContentScale.Inside
    ImageContentScaleSchema.FILL_BOUNDS -> ContentScale.FillBounds
}