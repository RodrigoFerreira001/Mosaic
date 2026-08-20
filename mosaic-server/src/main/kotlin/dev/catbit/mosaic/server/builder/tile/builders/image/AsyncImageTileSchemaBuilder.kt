package dev.catbit.mosaic.server.builder.tile.builders.image

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.AsyncImageTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible
import dev.catbit.mosaic.server.builder.placement.alignToCenter

internal class AsyncImageTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val model: AsyncImageTileSchema.Model,
    private val contentDescription: String?,
    private val contentScale: AsyncImageTileSchema.ContentScale,
    private val alpha: Float,
    private val clipToBounds: Boolean,
    private val alignment: AlignmentSchema.TwoDimensional
) : TileSchemaBuilder<AsyncImageTileSchema>() {

    override fun build() = AsyncImageTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        alpha = alpha,
        clipToBounds = clipToBounds,
        alignment = alignment
    )
}

/**
 * Renders a Coil `AsyncImage` that loads its content from [model] — a remote URL
 * ([urlImageModel]), raw bytes ([arrayOfBytesImageModel]) or a base64 string
 * ([base64ImageModel], decoded on the client before being handed to Coil). Dispatches
 * `onAsyncImageLoadStart` / `onAsyncImageLoadSuccess` / `onAsyncImageLoadFailure` on every load
 * state change, including reloads triggered by a [model] change, so they may fire more than once
 * over the tile's lifetime. The tile is not clickable and fires no display trigger — wrap it in
 * a `Box` or `Card` for tap handling. There is no built-in placeholder or error image: render one
 * yourself by reacting to the load triggers.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (load start, load success, load failure).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param model Image source — [urlImageModel], [arrayOfBytesImageModel] or [base64ImageModel].
 * @param contentDescription Accessibility description of the image. Defaults to none.
 * @param contentScale How the image is scaled to fit its bounds — [cropContentScale], [fitContentScale], [fillHeightContentScale], [fillWidthContentScale], [insideContentScale] or [fillBoundsContentScale]. Defaults to fit.
 * @param alpha Opacity applied to the image, from 0f to 1f. Defaults to 1f.
 * @param clipToBounds Whether the image is clipped to its layout bounds. Defaults to true.
 * @param alignment Alignment of the image within its bounds. Defaults to center.
 */
fun TileSchemaBuilderScope.AsyncImage(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    model: AsyncImageTileSchema.Model,
    contentDescription: String? = null,
    contentScale: AsyncImageTileSchema.ContentScale = fitContentScale(),
    alpha: Float = 1.0f,
    clipToBounds: Boolean = true,
    alignment: AlignmentSchema.TwoDimensional = alignToCenter()
) {
    addBuilder(
        AsyncImageTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            model = model,
            contentDescription = contentDescription,
            contentScale = contentScale,
            alpha = alpha,
            clipToBounds = clipToBounds,
            alignment = alignment
        )
    )
}

/** Crops the image to fill its bounds, preserving aspect ratio and cropping overflow. */
fun cropContentScale() = AsyncImageTileSchema.ContentScale.CROP

/** Scales the image to fit entirely within its bounds, preserving aspect ratio. */
fun fitContentScale() = AsyncImageTileSchema.ContentScale.FIT

/** Scales the image so its height fills the bounds, preserving aspect ratio. */
fun fillHeightContentScale() = AsyncImageTileSchema.ContentScale.FILL_HEIGHT

/** Scales the image so its width fills the bounds, preserving aspect ratio. */
fun fillWidthContentScale() = AsyncImageTileSchema.ContentScale.FILL_WIDTH

/** Scales the image down to fit its bounds only if it overflows them, preserving aspect ratio. */
fun insideContentScale() = AsyncImageTileSchema.ContentScale.INSIDE

/** Stretches the image to fill its bounds exactly, ignoring aspect ratio. */
fun fillBoundsContentScale() = AsyncImageTileSchema.ContentScale.FILL_BOUNDS


/** Loads the image from a remote [url]. */
fun urlImageModel(url: String) = AsyncImageTileSchema.Model.Url(url)

/** Loads the image from raw, already-decoded bytes. */
fun arrayOfBytesImageModel(byteArray: ByteArray) = AsyncImageTileSchema.Model.ArrayOfBytes(byteArray)

/** Loads the image from a base64-encoded string, decoded on the client before being handed to Coil. */
fun base64ImageModel(base64: String) = AsyncImageTileSchema.Model.Base64(base64)
