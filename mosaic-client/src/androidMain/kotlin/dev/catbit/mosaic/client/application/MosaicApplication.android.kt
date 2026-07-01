package dev.catbit.mosaic.client.application

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.effects.SingleEffect
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init

@Composable
internal actual fun PlatformWrapper(
    content: @Composable (() -> Unit)
) {
    val activity = LocalActivity.current as ComponentActivity
    SingleEffect { FileKit.init(activity) }

    content()
}