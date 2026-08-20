package dev.catbit.mosaic.client.ui.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.extensions.toBrush
import dev.catbit.mosaic.client.extensions.toComposeColor
import dev.catbit.mosaic.client.extensions.toComposeWindowInsets
import dev.catbit.mosaic.client.extensions.toPaddingValues
import dev.catbit.mosaic.client.extensions.toShape
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema

/**
 * Applies a tile's [StyleSchema] to this modifier, optionally making the tile interactive.
 *
 * When [onClick] and/or [onLongClick] is non-null a single `combinedClickable` is installed, so a
 * tile that only declares a long-press event still gets ripple feedback and accessibility
 * semantics without reacting to plain taps.
 */
@Composable
fun Modifier.styledWith(
    style: StyleSchema,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null
): Modifier = this
    .thenIfNotNull(style.windowInsets) { Modifier.windowInsetsPadding(it.toComposeWindowInsets()) }
    .thenIfNotNull(style.margin) { Modifier.padding(it.toPaddingValues()) }
    .then(Modifier.size(style.size))
    .thenIfNotNull(style.clip) { Modifier.clip(it.shape.toShape()) }
    .thenIfNotNull(style.background) { Modifier.background(brush = it.toBrush(), alpha = it.alpha) }
    .thenIf(onClick != null || onLongClick != null) {
        Modifier.combinedClickable(
            onClick = { onClick?.invoke() },
            onLongClick = onLongClick
        )
    }
    .thenIfNotNull(style.border) { border ->
        Modifier.border(
            width = border.thickness.dp,
            color = border.color.toComposeColor(),
            shape = border.radius?.toShape() ?: RectangleShape
        )
    }
    .thenIfNotNull(style.padding) { Modifier.padding(it.toPaddingValues()) }