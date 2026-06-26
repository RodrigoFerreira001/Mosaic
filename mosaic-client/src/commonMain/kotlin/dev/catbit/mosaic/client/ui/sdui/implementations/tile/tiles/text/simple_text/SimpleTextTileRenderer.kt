package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.simple_text

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import dev.catbit.mosaic.client.extensions.toComposeColor
import dev.catbit.mosaic.client.extensions.toFontFamily
import dev.catbit.mosaic.client.extensions.toFontStyle
import dev.catbit.mosaic.client.extensions.toFontWeight
import dev.catbit.mosaic.client.extensions.toTextAlign
import dev.catbit.mosaic.client.extensions.toTextAutoSize
import dev.catbit.mosaic.client.extensions.toTextDecoration
import dev.catbit.mosaic.client.extensions.toTextOverflow
import dev.catbit.mosaic.client.extensions.toTextStyle
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.text.SimpleTextTileSchema

object SimpleTextTileRenderer : TileRenderer<SimpleTextTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: SimpleTextTileSchema,
    ) {
        with(tileSchema) {
            Text(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style),
                text = text,
                color = color?.toComposeColor() ?: Color.Unspecified,
                autoSize = autoSize?.toTextAutoSize(),
                fontSize = fontSize?.sp ?: TextUnit.Unspecified,
                fontStyle = fontStyle?.toFontStyle(),
                fontWeight = fontWeight?.toFontWeight(),
                fontFamily = fontFamily?.toFontFamily(),
                letterSpacing = letterSpacing?.sp ?: TextUnit.Unspecified,
                textDecoration = textDecoration?.toTextDecoration(),
                textAlign = textAlign?.toTextAlign(),
                lineHeight = lineHeight?.sp ?: TextUnit.Unspecified,
                overflow = overflow?.toTextOverflow() ?: TextOverflow.Clip,
                softWrap = softWrap ?: true,
                maxLines = maxLines ?: Int.MAX_VALUE,
                minLines = minLines ?: 1,
                style = typography?.toTextStyle() ?: LocalTextStyle.current,
            )
        }
    }
}
