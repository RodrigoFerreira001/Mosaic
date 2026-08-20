package dev.catbit.mosaic.client.ui.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

/** Swaps the pointer to a hand cursor while hovering — a desktop/web-only affordance (a no-op on
 * touch platforms), used by `SearchBarTileRenderer` on its clickable icons since Material's own
 * `IconButton` doesn't set this by default. */
fun Modifier.handPointer() = this then Modifier.pointerHoverIcon(PointerIcon.Hand)