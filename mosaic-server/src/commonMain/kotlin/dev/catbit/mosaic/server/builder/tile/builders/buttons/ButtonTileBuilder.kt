package dev.catbit.mosaic.server.builder.tile.builders.buttons

import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.tiles.buttons.ButtonTileModel
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.style.StyleModelBuilder
import dev.catbit.mosaic.server.builder.event.EventModelBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileModelBuilder
import dev.catbit.mosaic.server.builder.tile.TileModelBuilderScope

class ButtonTileBuilder(
    private val id: String,
    private val events: EventModelBuilderScope.() -> Unit = {},
    private val style: StyleModelBuilder.StyleModelBuilderScope.() -> Unit = {},
    private val visibility: TileModel.Visibility,
    private val text: String,
    private val loading: Boolean
) : TileModelBuilder<ButtonTileModel> {

    override fun build() = ButtonTileModel(
        id = id,
        events = EventModelBuilderScope().apply(events).build(),
        style = StyleModelBuilder().apply { StyleModelBuilderScope().apply(style) }.build(),
        visibility = visibility,
        text = text,
        loading = loading
    )
}

fun TileModelBuilderScope.Button(
    id: String = randomUuid(),
    events: EventModelBuilderScope.() -> Unit = {},
    style: StyleModelBuilder.StyleModelBuilderScope.() -> Unit = {},
    visibility: TileModel.Visibility = TileModel.Visibility.VISIBLE,
    text: String,
    loading: Boolean = false
) {
    addBuilder(
        ButtonTileBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            text = text,
            loading = loading,
        )
    )
}