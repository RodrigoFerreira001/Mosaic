package dev.catbit.mosaic.client.ui.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

fun Modifier.handPointer() = this then Modifier.pointerHoverIcon(PointerIcon.Hand)