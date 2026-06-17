package dev.catbit.mosaic.server.builder.tile.builders.buttons

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.ButtonTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class ButtonTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val text: String,
    private val icon: IconSchema?,
    private val buttonType: ButtonTileSchema.Type,
    private val shape: ButtonTileSchema.Shape,
    private val loading: Boolean,
    private val enabled: Boolean,
    private val iconPosition: ButtonTileSchema.IconPosition
) : TileSchemaBuilder<ButtonTileSchema>() {

    override fun build() = ButtonTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        text = text,
        icon = icon,
        buttonType = buttonType,
        shape = shape,
        loading = loading,
        enabled = enabled,
        iconPosition = iconPosition
    )
}

fun TileSchemaBuilderScope.Button(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    text: String,
    icon: IconSchema? = null,
    buttonType: ButtonTileSchema.Type = ButtonTileSchema.Type.FILLED,
    shape: ButtonTileSchema.Shape = ButtonTileSchema.Shape.ROUNDED,
    loading: Boolean = false,
    enabled: Boolean = true,
    iconPosition: ButtonTileSchema.IconPosition = ButtonTileSchema.IconPosition.START
) {
    addBuilder(
        ButtonTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            text = text,
            icon = icon,
            buttonType = buttonType,
            shape = shape,
            loading = loading,
            enabled = enabled,
            iconPosition = iconPosition
        )
    )
}

fun filledButton() = ButtonTileSchema.Type.FILLED
fun elevatedButton() = ButtonTileSchema.Type.ELEVATED
fun filledTonalButton() = ButtonTileSchema.Type.FILLED_TONAL
fun outlinedButton() = ButtonTileSchema.Type.OUTLINED
fun textButton() = ButtonTileSchema.Type.TEXT

fun squareButton() = ButtonTileSchema.Shape.SQUARE
fun roundedButton() = ButtonTileSchema.Shape.ROUNDED

fun iconAtStart() = ButtonTileSchema.IconPosition.START
fun iconAtEnd() = ButtonTileSchema.IconPosition.END