package dev.catbit.mosaic.server.builder.tile.builders.inputs

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.RadioButtonTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class RadioButtonTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val selected: Boolean,
    private val enabled: Boolean,
    private val groupId: String
) : TileSchemaBuilder<RadioButtonTileSchema> {

    override fun build() = RadioButtonTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilder().apply { StyleSchemaBuilderScope().apply(style) }.build(),
        visibility = visibility,
        selected = selected,
        enabled = enabled,
        groupId = groupId
    )
}

fun TileSchemaBuilderScope.RadioButton(
    id: String = randomUuid(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit = {
        size(
            width = wrapHorizontally(),
            height = wrapVertically()
        )
    },
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    selected: Boolean = false,
    enabled: Boolean = true,
    groupId: String
) {
    addBuilder(
        RadioButtonTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            selected = selected,
            enabled = enabled,
            groupId = groupId
        )
    )
}
