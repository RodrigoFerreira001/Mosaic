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
): Modifier = this
    .thenIfNotNull(style.windowInsets) { Modifier.windowInsetsPadding(it.toComposeWindowInsets()) }
    .thenIfNotNull(style.margin) { Modifier.padding(it.toPaddingValues()) }
    .then(Modifier.size(style.size))
    .thenIfNotNull(style.clip) { Modifier.clip(it.shape.toShape()) }
    .thenIfNotNull(style.background) { Modifier.background(it.toComposeColor()) }
    .thenIfNotNull(onClick) { Modifier.clickable { it() } }
    .thenIfNotNull(style.border) { border ->
        Modifier.border(
            width = border.thickness.dp,
            color = border.color.toComposeColor(),
            shape = border.radius?.toShape() ?: RectangleShape
        )
    }
    .thenIfNotNull(style.padding) { Modifier.padding(it.toPaddingValues()) }