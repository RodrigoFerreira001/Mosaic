package dev.catbit.mosaic.server.builder.tile.builders.image

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.ImageTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class ImageTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
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
        visibility = visibility,
        resourceName = resourceName,
        contentDescription = contentDescription,
        contentScale = contentScale,
        alpha = alpha,
        alignment = alignment
    )
}

fun TileSchemaBuilderScope.Image(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    resourceName: String,
    contentDescription: String? = null,
    contentScale: ImageTileSchema.ContentScale = ImageTileSchema.ContentScale.FIT,
    alpha: Float = 1.0f,
    alignment: AlignmentSchema.TwoDimensional = AlignmentSchema.TwoDimensional.Center
) {
    addBuilder(
        ImageTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            resourceName = resourceName,
            contentDescription = contentDescription,
            contentScale = contentScale,
            alpha = alpha,
            alignment = alignment
        )
    )
}
