package dev.catbit.mosaic.server.builder.tile.builders.image

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.AsyncImageTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class AsyncImageTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val url: String,
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
        visibility = visibility,
        url = url,
        contentDescription = contentDescription,
        contentScale = contentScale,
        alpha = alpha,
        clipToBounds = clipToBounds,
        alignment = alignment
    )
}

fun TileSchemaBuilderScope.AsyncImage(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    url: String,
    contentDescription: String? = null,
    contentScale: AsyncImageTileSchema.ContentScale = AsyncImageTileSchema.ContentScale.FIT,
    alpha: Float = 1.0f,
    clipToBounds: Boolean = true,
    alignment: AlignmentSchema.TwoDimensional = AlignmentSchema.TwoDimensional.Center
) {
    addBuilder(
        AsyncImageTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            url = url,
            contentDescription = contentDescription,
            contentScale = contentScale,
            alpha = alpha,
            clipToBounds = clipToBounds,
            alignment = alignment
        )
    )
}

fun cropContentScale() = AsyncImageTileSchema.ContentScale.CROP
fun fitContentScale() = AsyncImageTileSchema.ContentScale.FIT
fun fillHeightContentScale() = AsyncImageTileSchema.ContentScale.FILL_HEIGHT
fun fillWidthContentScale() = AsyncImageTileSchema.ContentScale.FILL_WIDTH
fun insideContentScale() = AsyncImageTileSchema.ContentScale.INSIDE
fun fillBoundsContentScale() = AsyncImageTileSchema.ContentScale.FILL_BOUNDS
