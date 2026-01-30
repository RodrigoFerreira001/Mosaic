package dev.catbit.mosaic.client.ui.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.extensions.toComposeColor
import dev.catbit.mosaic.client.extensions.toComposeWindowInsets
import dev.catbit.mosaic.client.extensions.toPaddingValues
import dev.catbit.mosaic.client.extensions.toShape
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema

@Composable
fun Modifier.styledWith(
    style: StyleSchema,
    onClick: (() -> Unit)? = null
): Modifier =
    thenIfNotNull(style.windowInsets) { windowInsets ->
        Modifier.windowInsetsPadding(windowInsets.toComposeWindowInsets())
    }
        .thenIfNotNull(style.margin) { margin ->
            Modifier.padding(margin.toPaddingValues())
        }
        .then(Modifier.size(style.size))
        .thenIfNotNull(style.clip) { clip ->
            Modifier.clip(clip.shape.toShape())
        }
        .thenIfNotNull(style.background) { color ->
            background(color.toComposeColor())
        }
        .thenIfNotNull(style.border) { border ->
            Modifier.border(
                width = border.thickness.dp,
                color = border.color.toComposeColor(),
                shape = border.radius?.toShape() ?: RectangleShape
            )
        }
        .thenIfNotNull(onClick) {
            Modifier.clickable { it() }
        }
        .thenIfNotNull(style.padding) { padding ->
            Modifier.padding(padding.toPaddingValues())
        }