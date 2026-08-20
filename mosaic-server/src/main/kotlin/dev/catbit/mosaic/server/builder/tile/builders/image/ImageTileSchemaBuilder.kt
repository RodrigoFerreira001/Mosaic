package dev.catbit.mosaic.server.builder.tile.builders.image

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.ImageTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible
import dev.catbit.mosaic.server.builder.placement.alignToCenter

internal class ImageTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val resourceName: String,
    private val contentDescription: String?,
    private val contentScale: ImageTileSchema.ContentScale,
    private val alpha: Float,
    private val alignment: AlignmentSchema.TwoDimensional
) : TileSchemaBuilder<ImageTileSchema>() {

    override fun build() = ImageTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        resourceName = resourceName,
        contentDescription = contentDescription,
        contentScale = contentScale,
        alpha = alpha,
        alignment = alignment
    )
}

/**
 * Renders a Compose `Image` from a drawable bundled with the client application. [resourceName]
 * is looked up in the app's `DrawableResourcesHolder`; when no resource is registered under that
 * name **nothing is rendered at all** — there is no fallback and no error trigger. Dispatches no
 * triggers and is not clickable. Use `AsyncImage` for images that come from the network or from
 * raw bytes — this tile can only show assets that ship with the client.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile. Never fired, since the tile dispatches no triggers.
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param resourceName Name of the drawable resource looked up in the client's `DrawableResourcesHolder`.
 * @param contentDescription Accessibility description of the image. Defaults to none.
 * @param contentScale How the image is scaled to fit its bounds — [imageCropContentScale], [imageFitContentScale], [imageFillHeightContentScale], [imageFillWidthContentScale], [imageInsideContentScale] or [imageFillBoundsContentScale]. Defaults to fit.
 * @param alpha Opacity applied to the image, from 0f to 1f. Defaults to 1f.
 * @param alignment Alignment of the image within its bounds. Defaults to center.
 */
fun TileSchemaBuilderScope.Image(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    resourceName: String,
    contentDescription: String? = null,
    contentScale: ImageTileSchema.ContentScale = imageFitContentScale(),
    alpha: Float = 1.0f,
    alignment: AlignmentSchema.TwoDimensional = alignToCenter()
) {
    addBuilder(
        ImageTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            resourceName = resourceName,
            contentDescription = contentDescription,
            contentScale = contentScale,
            alpha = alpha,
            alignment = alignment
        )
    )
}

/** Crops the image to fill its bounds, preserving aspect ratio and cropping overflow. */
fun imageCropContentScale() = ImageTileSchema.ContentScale.CROP

/** Scales the image to fit entirely within its bounds, preserving aspect ratio. */
fun imageFitContentScale() = ImageTileSchema.ContentScale.FIT

/** Scales the image so its height fills the bounds, preserving aspect ratio. */
fun imageFillHeightContentScale() = ImageTileSchema.ContentScale.FILL_HEIGHT

/** Scales the image so its width fills the bounds, preserving aspect ratio. */
fun imageFillWidthContentScale() = ImageTileSchema.ContentScale.FILL_WIDTH

/** Scales the image down to fit its bounds only if it overflows them, preserving aspect ratio. */
fun imageInsideContentScale() = ImageTileSchema.ContentScale.INSIDE

/** Stretches the image to fill its bounds exactly, ignoring aspect ratio. */
fun imageFillBoundsContentScale() = ImageTileSchema.ContentScale.FILL_BOUNDS
